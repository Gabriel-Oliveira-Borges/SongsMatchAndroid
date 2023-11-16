package com.example.songmatch.mainMenu.presentation.model

import androidx.lifecycle.MutableLiveData


class JoinRoomViewState {
    var roomCode = MutableLiveData<String>()
    var action = MutableLiveData<Action>()

    sealed class Action {
        object NavigateToRoomFragment: Action()
    }
}