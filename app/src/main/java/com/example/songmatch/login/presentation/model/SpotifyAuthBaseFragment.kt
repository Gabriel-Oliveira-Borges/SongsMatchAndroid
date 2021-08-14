package com.example.songmatch.login.presentation.model

import com.example.songmatch.core.presentation.BaseFragment

 abstract class SpotifyAuthBaseFragment: BaseFragment() {
    open fun onSpotifyLoginError() {}

     open fun onSpotifyLoginSuccess() {}
}