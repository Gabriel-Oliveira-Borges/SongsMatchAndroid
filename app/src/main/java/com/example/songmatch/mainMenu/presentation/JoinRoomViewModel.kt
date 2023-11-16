package com.example.songmatch.mainMenu.presentation

import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.JoinRoomUseCase
import com.example.songmatch.mainMenu.presentation.model.JoinRoomViewAction
import com.example.songmatch.mainMenu.presentation.model.JoinRoomViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class JoinRoomViewModel @Inject constructor(
    private val joinRoomUseCase: JoinRoomUseCase
) : BaseViewModel<JoinRoomViewAction, JoinRoomViewState>() {
    override val viewState = JoinRoomViewState()

    override fun dispatchViewAction(action: JoinRoomViewAction) {

    }

    fun onJoinRoomPressed() {
        viewModelScope.launch {
            if (viewState.roomCode.value?.length == 5) {
                joinRoomUseCase(roomCode = viewState.roomCode.value!!)
                viewState.action.postValue(JoinRoomViewState.Action.NavigateToRoomFragment)
            } else {
//            TODO: Let user know it
            }
        }
    }
}