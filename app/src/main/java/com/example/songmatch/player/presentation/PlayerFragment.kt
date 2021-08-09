package com.example.songmatch.player.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.songmatch.login.presentation.presentation.CLIENT_ID
import com.example.songmatch.login.presentation.presentation.REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track

private var mSpotifyAppRemote: SpotifyAppRemote? = null

//    I will use this class later, when it will be possible to play the playlist directly from the app (So, I will keep the user longer using the app and monetize it more)
class PlayerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    private fun connectToSpotifyApp() {
        val connectionParams = ConnectionParams
            .Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this.context, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote

                    playerListener()
                    playSong()
                }

                override fun onFailure(throwable: Throwable) {
                    mSpotifyAppRemote = null
                    Log.e("MainActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    private fun playSong(uri: String = "spotify:track:1wsRitfRRtWyEapl0q22o8") {
        mSpotifyAppRemote?.playerApi?.play(uri)
    }

    private fun playerListener() {
        mSpotifyAppRemote
            ?.playerApi
            ?.subscribeToPlayerState()
            ?.setEventCallback { playerState: PlayerState ->
//                When there's any change in the player, this function gets called
                val track: Track? = playerState.track
                if (track != null) {
                    Log.d(
                        "MainActivity",
                        track.name.toString() + " by " + track.artist.name
                    )
                }
            }
    }

}