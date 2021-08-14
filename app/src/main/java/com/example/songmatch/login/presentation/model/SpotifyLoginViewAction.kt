package com.example.songmatch.login.presentation.model

import android.app.Activity

sealed class SpotifyLoginViewAction {
    class RequestLogin(val activity: Activity): SpotifyLoginViewAction()
}