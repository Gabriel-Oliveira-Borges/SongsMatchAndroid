package com.example.songmatch.login.presentation.model

import androidx.lifecycle.MutableLiveData

class SpotifyLoginViewState {
    val isLoading = MutableLiveData<Boolean>()
    sealed class Action {

    }
    val action = MutableLiveData<Action>()
}