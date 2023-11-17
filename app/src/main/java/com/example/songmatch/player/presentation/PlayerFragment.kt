package com.example.songmatch.player.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.songmatch.login.presentation.CLIENT_ID
import com.example.songmatch.login.presentation.REDIRECT_URI
import com.example.songmatch.core.presentation.BaseFragment
import com.example.songmatch.databinding.PlayerFragmentBinding
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlayerFragment: BaseFragment() {
    private val viewModel: PlayerViewModel by viewModels()
    lateinit var binding: PlayerFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        connectToSpotifyApp()

        //TODO: Improve it
        (arguments?.get("roomCode") as? String)?.let {
            viewModel.dispatchViewAction(PlayerViewAction.OnInit(it))
        }

        val view = PlayerFragmentBinding.inflate(inflater, container, false).apply {
            this@PlayerFragment.binding = this
            viewModel = this@PlayerFragment.viewModel
            lifecycleOwner = this@PlayerFragment.viewLifecycleOwner
        }.root

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        SpotifyAppRemote.disconnect(viewModel.mSpotifyAppRemote)
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
                    viewModel.dispatchViewAction(PlayerViewAction.SetSpotifyAppRemote(spotifyAppRemote))
                }

                override fun onFailure(throwable: Throwable) {
                    //TODO: Impossible to play the songs
                    Log.e("PlayerFragment", throwable.message, throwable)
                }
            })
    }

    private fun listenToCurrentTrack() {
        viewModel.viewState.currentTrack.observe(this) {
            
        }
    }

    companion object {
        fun newInstance() = PlayerFragment()
    }

}