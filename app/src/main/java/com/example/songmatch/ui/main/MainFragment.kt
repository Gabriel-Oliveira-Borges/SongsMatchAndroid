package com.example.songmatch.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.songmatch.R
import com.example.songmatch.SPOTIFY_TOKEN
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

const val CLIENT_ID = "d9451fbe5bbc4427abf4299070e2dbe1"
const val REDIRECT_URI = "http://localhost:8888/callback"
const val REQUEST_CODE = 1337

class MainFragment : Fragment() {
    val sharedPreferences by lazy { requireContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE) }
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }

    private fun getFromSharedPreferences() {
        Log.d("MainActivity", sharedPreferences.getString(SPOTIFY_TOKEN, null).toString())
    }

    private fun logInToSpotify() {
        val builder: AuthenticationRequest.Builder =
            AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)

        builder.setScopes(arrayOf("streaming", "user-top-read", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "playlist-modify-private", "user-follow-read", "user-library-read", "user-read-email"))
        builder.setShowDialog(true)
        val request: AuthenticationRequest = builder.build()

        AuthenticationClient.openLoginActivity(this.requireActivity(), REQUEST_CODE, request)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}