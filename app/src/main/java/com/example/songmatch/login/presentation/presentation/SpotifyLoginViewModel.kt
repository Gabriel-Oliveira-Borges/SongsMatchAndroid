package com.example.songmatch.login.presentation.presentation

import android.app.Activity
import com.example.songmatch.BaseViewModel
import com.example.songmatch.login.presentation.presentation.model.SpotifyLoginViewAction
import com.example.songmatch.login.presentation.presentation.model.SpotifyLoginViewAction.RequestLogin
import com.example.songmatch.login.presentation.presentation.model.SpotifyLoginViewState
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

class SpotifyLoginViewModel : BaseViewModel<SpotifyLoginViewAction, SpotifyLoginViewState>() {
    override val viewState =
        SpotifyLoginViewState()

    override fun dispatchViewAction(action: SpotifyLoginViewAction) {
        when (action) {
            is RequestLogin -> requestLogin(activity = action.activity)
        }
    }

    // TODO: Mudar tudo isso para um Use Case
    private fun requestLogin(activity: Activity) {
        val builder: AuthenticationRequest.Builder =
            AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI // TODO: Mudar isso e ver pra que serve e como configurar corretamente
            )
        builder.setScopes(arrayOf("streaming", "user-top-read", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "playlist-modify-private", "user-follow-read", "user-library-read", "user-read-email"))
        builder.setShowDialog(true)
        val request: AuthenticationRequest = builder.build()

        // This will return its data in the `onActivityResult` of MainActivity
        AuthenticationClient.openLoginActivity(activity, SPOTIFY_LOGIN_REQUEST_CODE, request)
    }
}