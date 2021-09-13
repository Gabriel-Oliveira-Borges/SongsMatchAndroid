package com.example.songmatch.player.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.songmatch.core.framework.room.daos.TrackDao
import com.example.songmatch.core.framework.room.entities.TrackEntity
import com.example.songmatch.core.useCase.SaveSpotifyUserUseCase
import com.example.songmatch.databinding.SpotifyLoginFragmentBinding
import com.example.songmatch.login.presentation.CLIENT_ID
import com.example.songmatch.login.presentation.REDIRECT_URI
import com.example.songmatch.login.presentation.SpotifyLoginFragment
import com.example.songmatch.login.presentation.SpotifyLoginViewModel
import com.example.songmatch.login.presentation.listener.SpotifyLoginFragmentListener
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private var mSpotifyAppRemote: SpotifyAppRemote? = null

@AndroidEntryPoint
//  TODO:  I will use this class later, when it will be possible to play the playlist directly from the app (So, I will keep the user longer using the app and monetize it more)
class PlayerFragment: Fragment(), SpotifyLoginFragmentListener {
//    TODO: Remove all of this testing things
    private val viewModel: SpotifyLoginViewModel by viewModels()
    private lateinit var binding: SpotifyLoginFragmentBinding
    @Inject
    lateinit var trackDao: TrackDao
    private val tracks = mutableListOf<TrackEntity>()
    private var songIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return SpotifyLoginFragmentBinding.inflate(inflater, container, false).apply {
            binding = this
            viewModel = this@PlayerFragment.viewModel
            listener = this@PlayerFragment
        }.root
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
                    lifecycleScope.launch {
                        tracks.addAll(trackDao.getAllTracks())
                    }
                    playerListener()
                    playSong(tracks[songIndex].uri)
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

    override fun onLoginPressed() {
        if (mSpotifyAppRemote == null)
            connectToSpotifyApp()
        else {
            songIndex++
            playSong(tracks[songIndex].uri)
        }
    }

}