package com.example.songmatch.login.presentation.presentation.model

import android.app.Activity

sealed class SpotifyLoginViewAction {
    class RequestLogin(val activity: Activity): SpotifyLoginViewAction()
}