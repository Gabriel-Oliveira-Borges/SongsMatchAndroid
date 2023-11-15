package com.example.songmatch.login.presentation.model

import androidx.lifecycle.MutableLiveData

class SpotifyLoginViewState {
    sealed class Action {

    }
    val action = MutableLiveData<Action>()
}