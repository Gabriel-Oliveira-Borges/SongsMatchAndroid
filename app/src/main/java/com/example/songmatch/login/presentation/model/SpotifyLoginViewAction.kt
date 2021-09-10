package com.example.songmatch.login.presentation.model

import android.app.Activity

sealed class SpotifyLoginViewAction {
    object RequestLogin : SpotifyLoginViewAction()
}