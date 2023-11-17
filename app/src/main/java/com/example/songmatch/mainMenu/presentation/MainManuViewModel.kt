package com.example.songmatch.mainMenu.presentation

import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.CreateRoomUseCase
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import com.example.songmatch.core.useCase.UploadUserTracksUseCase
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewAction
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewState
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewState.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class MainManuViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val uploadUserTracksUseCase: UploadUserTracksUseCase,
    private val createRoomUseCase: CreateRoomUseCase
): BaseViewModel<MainMenuViewAction, MainMenuViewState>() {
    override val viewState = MainMenuViewState()

    override fun dispatchViewAction(action: MainMenuViewAction) {
        when (action) {
            is MainMenuViewAction.Init -> onInit()
        }
    }

    fun onCreateRoom() {
        viewModelScope.launch {
            createRoomUseCase().handleResult(
                onSuccess = {
                    viewState.navigationAction.postValue(MainMenuViewState.Action.NavigateToRoom)
                },
                onError = {
//                    TODO: let user know it
                }
            )
        }
    }

    fun onJoinRoom() {
        viewState.navigationAction.postValue(MainMenuViewState.Action.NavigateToJoinRoom)
    }

    private fun onInit() {
        getCurrentUser()
        uploadTracks()
    }

    private fun uploadTracks() {
        viewModelScope.launch {
            viewState.state.postValue(State.UploadingTracks)
            uploadUserTracksUseCase()
                .onSuccess {
                    viewState.state.postValue(State.Ready)
                }
                .onError {
                    uploadTracks()
                }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase().handleResult()

            if (user != null) {
                viewState.greetings.postValue("Olá, ${user.name}")
                if (!user.currentRoom.isNullOrEmpty()) {
                    viewState.navigationAction.postValue(MainMenuViewState.Action.NavigateToRoom)
                }
            }
        }
    }
}