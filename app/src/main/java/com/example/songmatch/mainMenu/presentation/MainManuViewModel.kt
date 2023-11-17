package com.example.songmatch.mainMenu.presentation

import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.CreateRoomUseCase
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import com.example.songmatch.core.useCase.UploadUserTracksUseCase
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewAction
import com.example.songmatch.mainMenu.presentation.model.MainMenuViewState
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
            viewState.isLoading.postValue(true)
            createRoomUseCase().handleResult(
                onSuccess = {
                    viewState.navigationAction.postValue(MainMenuViewState.Action.NavigateToRoom)
                },
                onError = {
//                    TODO: let user know it
                }, onFinish = {
                    viewState.isLoading.postValue(false)
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
            viewState.isLoading.postValue(true)
            uploadUserTracksUseCase()
                .onSuccess {
                    viewState.isLoading.postValue(false)
                }
                .onError {
                    uploadTracks()
                }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            viewState.isLoading.postValue(true)
            val user = getCurrentUserUseCase().handleResult()
            viewState.isLoading.postValue(false)
            if (user != null) {
                viewState.greetings.postValue("Olá, ${user.name}")
                if (!user.currentRoom.isNullOrEmpty()) {
                    viewState.navigationAction.postValue(MainMenuViewState.Action.NavigateToRoom)
                }
            }
        }
    }
}