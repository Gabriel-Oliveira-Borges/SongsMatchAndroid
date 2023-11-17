package com.example.songmatch.presentation.model

import androidx.lifecycle.MutableLiveData

class SpotifyLoginViewState {
    val isLoading = MutableLiveData<Boolean>()
    sealed class Action {

    }
    val action = MutableLiveData<Action>()
}