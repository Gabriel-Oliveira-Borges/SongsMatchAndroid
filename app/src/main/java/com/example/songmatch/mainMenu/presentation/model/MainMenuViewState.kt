package com.example.songmatch.mainMenu.presentation.model

import androidx.lifecycle.MutableLiveData

class MainMenuViewState {
    val greetings = MutableLiveData<String>()
    val navigationAction = MutableLiveData<Action>()
    val state = MutableLiveData<State>()

    //TODO: Enable main menu buttons only when state == Ready
    sealed class State {
        object UploadingTracks: State()
        object Ready: State()
    }

    sealed class Action {
        object NavigateToJoinRoom : Action()
        object NavigateToRoom : Action()
    }
}