package com.example.songmatch

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.songmatch.login.presentation.presentation.SpotifyLoginFragment
import com.example.songmatch.login.presentation.presentation.SPOTIFY_LOGIN_REQUEST_CODE
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse

const val SPOTIFY_TOKEN = "SPOTIFY_TOKEN"
class MainActivity : AppCompatActivity() {
    val sharedPreferences by lazy { getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE) }

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
                        // TODO: Não usar o sharedPreferences diretamente na activity. Esse token é válido por apenas 1 hora. Quando eu precisar atualizar as músicas, eu vou precisar pedir novamente pelo login. A parte boa é que fica salvo
                        with(sharedPreferences.edit()) {
                            putString(SPOTIFY_TOKEN, response.accessToken)
                            apply()
                        }
                    }
                    AuthenticationResponse.Type.ERROR -> {
                        // TODO: Avisar ao usuário que deu ruim!
                    }
                    else -> {
                        // TODO: Avisar ao usuário que deu ruim!
                    }
                }
            }
        }
    }
}