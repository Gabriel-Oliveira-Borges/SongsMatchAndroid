package com.example.songmatch.login.useCase

import android.content.Context
import com.example.songmatch.AppApplication
import com.example.songmatch.login.presentation.CLIENT_ID
import com.example.songmatch.login.presentation.REDIRECT_URI
import com.example.songmatch.login.presentation.SPOTIFY_LOGIN_REQUEST_CODE
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface LoginToSpotifyUseCase {
    operator fun invoke()
}

private val SPOTIFY_AUTH_SCOPES = arrayOf(
    "streaming",
    "user-top-read",
    "user-read-playback-state",
    "user-modify-playback-state",
    "user-read-currently-playing",
    "playlist-modify-private",
    "user-follow-read",
    "user-library-read",
    "user-read-email",
    "user-read-private"
)

class LoginToSpotifyUseCaseImp @Inject constructor(
    @ApplicationContext private val context: Context
) : LoginToSpotifyUseCase {
    override operator fun invoke() {
        val builder: AuthenticationRequest.Builder =
            AuthenticationRequest.Builder(
                CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI
            )
        builder.setScopes(SPOTIFY_AUTH_SCOPES)
        builder.setShowDialog(true)
        val request: AuthenticationRequest = builder.build()

        (context.applicationContext as AppApplication).getCurrentActivity()?.let {
            AuthenticationClient.openLoginActivity(it, SPOTIFY_LOGIN_REQUEST_CODE, request)
        }
    }
}