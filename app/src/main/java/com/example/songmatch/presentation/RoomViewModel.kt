package com.example.songmatch.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.presentation.model.BaseViewModel
import com.example.songmatch.useCase.CreatePlaylistUseCase
import com.example.songmatch.useCase.LeaveRoomUseCase
import com.example.songmatch.useCase.ListenToCurrentRoomUseCase
import com.example.songmatch.presentation.model.RoomViewAction
import com.example.songmatch.presentation.model.RoomViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val listenToCurrentRoomUseCase: ListenToCurrentRoomUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val leaveRoomUseCase: LeaveRoomUseCase
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
                viewState.isLoading.value = true
                createPlaylistUseCase(it).onSuccess {
                    Log.d("ad", "Playlist criada")
                }.onError {
                    Log.d("ad", "Erro ao criar playlist")
                }.onFinish {
                    viewState.isLoading.value = false
                }
            }
        }
    }

    fun onLeaveRoom() {
        viewModelScope.launch {
            viewState.isLoading.value = true
            leaveRoomUseCase().onSuccess {
                viewState.action.postValue(RoomViewState.Action.GoToMainMenuFragment)
            }.onFinish {
                viewState.isLoading.value = false
            }
        }
    }

    private fun listenToCurrentRoom() {
        viewModelScope.launch {
            viewState.isLoading.value = true
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
                    viewState.isLoading.value = false
                }.onError {
                    listenToCurrentRoom()
                }
            }
        }
    }
}