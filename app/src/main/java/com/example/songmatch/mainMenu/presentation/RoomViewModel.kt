package com.example.songmatch.mainMenu.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.CreatePlaylistUseCase
import com.example.songmatch.core.useCase.ListenToCurrentRoomUseCase
import com.example.songmatch.mainMenu.presentation.model.RoomViewAction
import com.example.songmatch.mainMenu.presentation.model.RoomViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val listenToCurrentRoomUseCase: ListenToCurrentRoomUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase
): BaseViewModel<RoomViewAction, RoomViewState>() {
    override val viewState = RoomViewState()

    override fun dispatchViewAction(action: RoomViewAction) {
        when (action) {
            is RoomViewAction.ListenToCurrentRoom -> listenToCurrentRoom()
        }
    }

    fun onCreatePlaylist() {
        viewModelScope.launch {
            viewState.room.value?.let {
                createPlaylistUseCase(it).onSuccess {
                    Log.d("ad", "Playlist criada")
                }.onError {
                    Log.d("ad", "Erro ao criar playlist")
                }
            }
        }
    }

    fun onLeaveRoom() {
        // TODO: Implement it
    }

    private fun listenToCurrentRoom() {
        viewModelScope.launch {
            listenToCurrentRoomUseCase()?.collect {result ->
                result.onSuccess {room ->
                    viewState.room.postValue(room)
                    viewState.subtitle.postValue("Pessoas na sala ${room.usersToken.size}")
                    viewState.title.postValue("Código da sala ${room.roomCode.toString()}")

                    if (room.playlistCreated) {
                        viewState.action.postValue(
                            RoomViewState.Action.OpenPlayerFragment(room.roomCode)
                        )
                    }
                }.onError {
                    listenToCurrentRoom()
                }
            }
        }
    }
}