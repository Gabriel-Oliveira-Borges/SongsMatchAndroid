package com.example.songmatch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.songmatch.core.useCase.SaveUserSpotifyTokenUseCase
import com.example.songmatch.login.presentation.SpotifyLoginFragment
import com.example.songmatch.login.presentation.SPOTIFY_LOGIN_REQUEST_CODE
import com.example.songmatch.login.presentation.model.SpotifyAuthBaseFragment
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var saveSpotifyToken: SaveUserSpotifyTokenUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SpotifyLoginFragment.newInstance())
                .commitNow()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SPOTIFY_LOGIN_REQUEST_CODE -> {
                val response =
                    AuthenticationClient.getResponse(resultCode, data)
                when (response.type) {
                    AuthenticationResponse.Type.TOKEN -> {
                        saveSpotifyToken(
                            token = response.accessToken,
                            expiresIn = response.expiresIn
                        )
                        notifyFragments(successful = true)
                    }
                    AuthenticationResponse.Type.ERROR -> {
                        notifyFragments(successful = false)
                    }
                    else -> {
                        notifyFragments(successful = false)
                    }
                }
            }
        }
    }

    private fun getAllSpotifyAuthFragment(): List<SpotifyAuthBaseFragment> {
        return supportFragmentManager.fragments.filterIsInstance<SpotifyAuthBaseFragment>()
    }

    private fun notifyFragments(successful: Boolean) {
        getAllSpotifyAuthFragment().forEach {
            if (successful)
                it.onSpotifyLoginSuccess()
            else
                it.onSpotifyLoginError()
        }
    }
}