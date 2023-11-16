package com.example.songmatch.mainMenu.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.ListenToCurrentRoomUseCase
import com.example.songmatch.mainMenu.presentation.model.RoomViewAction
import com.example.songmatch.mainMenu.presentation.model.RoomViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val listenToCurrentRoomUseCase: ListenToCurrentRoomUseCase

): BaseViewModel<RoomViewAction, RoomViewState>() {
    override val viewState = RoomViewState()

    override fun dispatchViewAction(action: RoomViewAction) {
        when (action) {
            is RoomViewAction.ListenToCurrentRoom -> listenToCurrentRoom()
        }
    }

    private fun listenToCurrentRoom() {
        viewModelScope.launch {
            listenToCurrentRoomUseCase()?.collect {result ->
                result.onSuccess {room ->
                    viewState.room = room
                }.onError {
                    listenToCurrentRoom()
                }
            }
        }
    }
}