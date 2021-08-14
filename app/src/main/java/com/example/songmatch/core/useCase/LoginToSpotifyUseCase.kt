package com.example.songmatch.core.useCase

import android.app.Activity
import com.example.songmatch.login.presentation.CLIENT_ID
import com.example.songmatch.login.presentation.REDIRECT_URI
import com.example.songmatch.login.presentation.SPOTIFY_LOGIN_REQUEST_CODE
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import javax.inject.Inject

interface LoginToSpotifyUseCase {
    operator fun invoke(receiverActivity: Activity)
}

private val SPOTIFY_AUTH_SCOPES = arrayOf("streaming", "user-top-read", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "playlist-modify-private", "user-follow-read", "user-library-read", "user-read-email")

class LoginToSpotifyUseCaseImp @Inject constructor(
): LoginToSpotifyUseCase {
    override operator fun invoke(receiverActivity: Activity) {
        val builder: AuthenticationRequest.Builder =
            AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI // TODO: Mudar isso e ver pra que serve e como configurar corretamente
            )
        builder.setScopes(SPOTIFY_AUTH_SCOPES)
        builder.setShowDialog(true)
        val request: AuthenticationRequest = builder.build()

        // This will return its data in the `onActivityResult` of MainActivity
        AuthenticationClient.openLoginActivity(receiverActivity, SPOTIFY_LOGIN_REQUEST_CODE, request)
    }
}