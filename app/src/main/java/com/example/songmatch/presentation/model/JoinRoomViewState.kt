package com.example.songmatch.presentation.model

import androidx.lifecycle.MutableLiveData


class JoinRoomViewState {
    var roomCode = MutableLiveData<String>()
    var action = MutableLiveData<Action>()
    var isLoading = MutableLiveData<Boolean>()

    sealed class Action {
        object NavigateToRoomFragment: Action()
    }
}