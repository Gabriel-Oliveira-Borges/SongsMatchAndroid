package com.example.songmatch.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.presentation.model.BaseViewModel
import com.example.songmatch.useCase.JoinRoomUseCase
import com.example.songmatch.presentation.model.JoinRoomViewAction
import com.example.songmatch.presentation.model.JoinRoomViewState
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
                viewState.isLoading.value = true
                joinRoomUseCase(roomCode = viewState.roomCode.value!!).onSuccess {
                    viewState.action.postValue(JoinRoomViewState.Action.NavigateToRoomFragment)
                }.onError {
                    Log.d("Joinroom error", it)
                }.onFinish {
                    viewState.isLoading.value = false
                }
            } else {
//            TODO: Let user know it
            }
        }
    }
}