package com.example.songmatch.mainMenu.presentation.model

import androidx.lifecycle.MutableLiveData

class MainMenuViewState {
    val greetings = MutableLiveData<String>()
    val navigationAction = MutableLiveData<Action>()
    var isLoading = MutableLiveData<Boolean>()

    sealed class Action {
        object NavigateToJoinRoom : Action()
        object NavigateToRoom : Action()
    }
}