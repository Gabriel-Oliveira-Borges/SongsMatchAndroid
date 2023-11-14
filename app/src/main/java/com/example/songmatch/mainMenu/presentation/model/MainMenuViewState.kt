package com.example.songmatch.mainMenu.presentation.model

import androidx.lifecycle.MutableLiveData

class MainMenuViewState {
    val greetings = MutableLiveData<String>()
    val action = MutableLiveData<Action>()

    sealed class Action {
        object NavigateToJoinRoom : Action()
    }
}