package com.example.songmatch.player.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.songmatch.R
import com.example.songmatch.core.presentation.BaseFragment
import com.example.songmatch.databinding.PlayerFragmentBinding
import com.example.songmatch.login.presentation.CLIENT_ID
import com.example.songmatch.login.presentation.REDIRECT_URI
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.squareup.picasso.Picasso
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
        observeActions()

        //TODO: Improve it
        (arguments?.get("roomCode") as? String)?.let {
            viewModel.dispatchViewAction(PlayerViewAction.OnInit(it))
        }

        val view = PlayerFragmentBinding.inflate(inflater, container, false).apply {
            this@PlayerFragment.binding = this
            viewModel = this@PlayerFragment.viewModel
            lifecycleOwner = this@PlayerFragment.viewLifecycleOwner
        }.root

        listenToCurrentTrack()

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
        viewModel.viewState.currentTrack.observe(viewLifecycleOwner) {
            Picasso
                .get()
                .load(it.albumImageUri)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.imageView)
        }
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner) {
            when (it) {
                is PlayerViewState.Action.OpenSpotifyApp -> sendSpotifyIntent(it.uri)
                is PlayerViewState.Action.GoToMainMenuFragment -> navController.popBackStack(R.id.roomSelectionFragment, true)
            }
        }
    }

    private fun sendSpotifyIntent(uri: String) {
        val pm: PackageManager = activity?.packageManager ?: return
        val isSpotifyInstalled: Boolean = try {
            pm.getPackageInfo("com.spotify.music", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        if (isSpotifyInstalled) {
            val branchLink =
                "https://spotify.link/content_linking?~campaign=" + context?.packageName + "&\$deeplink_path=" + uri + "&\$fallback_url=" + uri
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(branchLink)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() = PlayerFragment()
    }

}