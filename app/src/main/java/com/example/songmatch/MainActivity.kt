package com.example.songmatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.songmatch.ui.main.MainFragment
import com.example.songmatch.ui.main.REQUEST_CODE
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
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("MainActivity", requestCode.toString())
        Log.e("MainActivity", resultCode.toString())
        when (requestCode) {
            REQUEST_CODE -> {
                val response =
                    AuthenticationClient.getResponse(resultCode, data)
                when (response.type) {
                    AuthenticationResponse.Type.TOKEN -> {
                        with(sharedPreferences.edit()) {
                            putString(SPOTIFY_TOKEN, response.accessToken)
                            apply()
                        }
                    }
                    AuthenticationResponse.Type.ERROR -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }
}