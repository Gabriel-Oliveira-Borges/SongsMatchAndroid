package com.example.songmatch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.songmatch.core.useCase.SaveSpotifyUserUseCase
import com.example.songmatch.login.presentation.SPOTIFY_LOGIN_REQUEST_CODE
import com.example.songmatch.login.presentation.model.SpotifyAuthBaseFragment
import com.example.songmatch.player.presentation.PlayerFragment
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var saveSpotifyUser: SaveSpotifyUserUseCase

    private lateinit var appApplication: AppApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlayerFragment.newInstance())
                .commitNow()

            appApplication = this.applicationContext as AppApplication
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMainActivityReferencesInApplication()
    }

    override fun onResume() {
        super.onResume()
        appApplication.setCurrentActivity(this)
    }

    override fun onPause() {
        super.onPause()
        clearMainActivityReferencesInApplication()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SPOTIFY_LOGIN_REQUEST_CODE -> {
                val response =
                    AuthenticationClient.getResponse(resultCode, data)
                when (response.type) {
                    AuthenticationResponse.Type.TOKEN -> {
                        lifecycleScope.launch {
                            saveSpotifyUser(
                                token = response.accessToken,
                                expiresIn = response.expiresIn,
                                name = null
                            ).onError {
                                notifyFragments(successful = false)
                            }.onSuccess {
                                notifyFragments(successful = true)
                            }
                        }
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

    private fun clearMainActivityReferencesInApplication() {
        val currentActivity = appApplication.getCurrentActivity()

        if (this == currentActivity) {
            appApplication.setCurrentActivity(null)
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