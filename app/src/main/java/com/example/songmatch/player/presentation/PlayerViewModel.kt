package com.example.songmatch.player.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.CreatePlaylistInSpotifyUseCase
import com.example.songmatch.core.useCase.GetPlaylistUseCase
import com.example.songmatch.core.useCase.GetTrackDetailsUseCase
import com.example.songmatch.core.useCase.LeaveRoomUseCase
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val getTrackDetailsUseCase: GetTrackDetailsUseCase,
    private val createPlaylistInSpotifyUseCase: CreatePlaylistInSpotifyUseCase,
    private val leaveRoomUseCase: LeaveRoomUseCase
): BaseViewModel<PlayerViewAction, PlayerViewState>()  {
    var mSpotifyAppRemote: SpotifyAppRemote? = null

    override val viewState = PlayerViewState()

    override fun dispatchViewAction(action: PlayerViewAction) {
        when (action) {
            is PlayerViewAction.SetSpotifyAppRemote -> {
                mSpotifyAppRemote = action.mSpotifyAppRemote
                viewState.isPlaying.postValue(false)
                setPlayerListener()
            }
            is PlayerViewAction.OnInit -> {
                viewState.roomCode.postValue(action.roomCode)
                getPlaylist(action.roomCode)
            }
        }
    }

    fun nextTrack() {
        viewState.trackIndex += 1
        playTrack(viewState.tracksUri[viewState.trackIndex])
    }

    fun previousTrack() {
        viewState.trackIndex -= 1
        playTrack(viewState.tracksUri[viewState.trackIndex])
    }

    fun toggleTrack() {
        viewState.isPlaying.value?.let {
            if (it) {
                mSpotifyAppRemote?.playerApi?.pause()
            } else {
                mSpotifyAppRemote?.playerApi?.resume()
            }
            viewState.isPlaying.postValue(it.toggle())
        }
    }

    fun onCreatePlaylist() {
        viewModelScope.launch {
            viewState.isLoading.value = true
            createPlaylistInSpotifyUseCase(viewState.tracksUri, viewState.roomCode.value!!)
                .onSuccess {
                    it?.let {
                        viewState.action.postValue(PlayerViewState.Action.OpenSpotifyApp(it))
                    }
                    Log.d("ashda", "Playlistcriada")
                }
                .onError {
                    Log.d("ashda", "Erro ao criar playlist")
                }.onFinish {
                    viewState.isLoading.value = false
                }

        }
    }

    fun onLeaveRoom() {
        viewModelScope.launch {
            viewState.isLoading.value = true
            leaveRoomUseCase().onSuccess {
                viewState.action.postValue(PlayerViewState.Action.GoToMainMenuFragment)
            }.onFinish {
                viewState.isLoading.value = false
            }
        }
    }

    private fun playTrack(uri: String) {
        mSpotifyAppRemote?.playerApi?.play(uri)
    }

    private fun getPlaylist(roomCode: String) {
        viewModelScope.launch {
            viewState.isLoading.value = true
            getPlaylistUseCase(roomCode)
                .onSuccess {
                    viewState.tracksUri = it.tracksUri
                    prepareFirstTrack()
                }
                .onError {
//                    TODO: Let user know it
                }
                .onFinish {
                    viewState.isLoading.value = false
                }
        }
    }

//    When there's any change in the player, this function gets called
    private fun setPlayerListener() {
        mSpotifyAppRemote
            ?.playerApi
            ?.subscribeToPlayerState()
            ?.setEventCallback { playerState: PlayerState ->
                viewState.isPlaying.postValue(!playerState.isPaused)

                val track: Track? = playerState.track

                if (track != null && track.uri != viewState.spotifyTrack.value?.uri) {
                    viewState.spotifyTrack.postValue(playerState.track)
                    getTrackDetails(track.uri)
                }
            }
    }

    private fun getTrackDetails(trackUri: String) {
        viewModelScope.launch {
            getTrackDetailsUseCase(trackUri).onSuccess {
                viewState.currentTrack.value = it
            }
        }
    }

    private fun prepareFirstTrack() {
        viewState.tracksUri.firstOrNull()?.apply {
            playTrack(this)
        }
    }

}

fun Boolean.toggle() = !this