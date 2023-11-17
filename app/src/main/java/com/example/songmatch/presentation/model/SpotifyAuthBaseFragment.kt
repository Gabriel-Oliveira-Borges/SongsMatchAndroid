package com.example.songmatch.presentation.model

abstract class SpotifyAuthBaseFragment: BaseFragment() {
    open fun onSpotifyLoginError() {}

     open fun onSpotifyLoginSuccess() {}
}