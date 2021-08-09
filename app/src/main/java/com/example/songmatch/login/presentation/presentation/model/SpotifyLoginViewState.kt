package com.example.songmatch.login.presentation.presentation.model

import androidx.lifecycle.MutableLiveData

class SpotifyLoginViewState {
    sealed class Action {

    }
    val action = MutableLiveData<Action>() //todo: Mudar isso para ser `SingleLiveEvent` e não bugar a navegação como foi no bills manager
}