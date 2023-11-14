package com.example.songmatch.mainMenu.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewAction
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class MainManuViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): BaseViewModel<MainMenuViewAction, MainMenuViewState>() {
    override val viewState = MainMenuViewState()

    override fun dispatchViewAction(action: MainMenuViewAction) {
        when (action) {
            is MainMenuViewAction.Init -> getCurrentUser()
        }
    }

    fun onCreateRoom() {
        Log.d("RoomSelection","on Create Room")
    }

    fun onJoinRoom() {
        viewState.action.postValue(MainMenuViewState.Action.NavigateToJoinRoom)
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase().handleResult()

            if (user != null) {
                viewState.greetings.postValue("Olá, ${user.name}")
            }
        }
    }
}