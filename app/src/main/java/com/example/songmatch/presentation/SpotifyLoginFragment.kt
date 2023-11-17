package com.example.songmatch.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.songmatch.NavGraphDirections
import com.example.songmatch.presentation.listener.SpotifyLoginFragmentListener
import com.example.songmatch.databinding.SpotifyLoginFragmentBinding
import com.example.songmatch.presentation.model.SpotifyAuthBaseFragment
import com.example.songmatch.presentation.model.SpotifyLoginViewAction
import dagger.hilt.android.AndroidEntryPoint

const val CLIENT_ID = "d9451fbe5bbc4427abf4299070e2dbe1"
const val REDIRECT_URI = "http://localhost:8888/callback"
const val SPOTIFY_LOGIN_REQUEST_CODE = 1337

@AndroidEntryPoint
class SpotifyLoginFragment : SpotifyAuthBaseFragment(), SpotifyLoginFragmentListener {
    private val viewModel: SpotifyLoginViewModel by viewModels()
    private lateinit var binding: SpotifyLoginFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        observeActions()
        return SpotifyLoginFragmentBinding.inflate(inflater).apply {
            binding = this
            viewModel = this@SpotifyLoginFragment.viewModel
            listener = this@SpotifyLoginFragment
        }.root
    }


    override fun onLoginPressed() {
        viewModel.dispatchViewAction(
            SpotifyLoginViewAction.RequestLogin
        )
    }

    override fun onSpotifyLoginError() {
        viewModel.viewState.isLoading.value = false
        Log.d("Blah", "Erro ao fazer login")
    }

    override fun onSpotifyLoginSuccess() {
        viewModel.viewState.isLoading.value = false
        navController.navigate(NavGraphDirections.actionGlobalToMainNavGraph())
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner, Observer {action ->
            when (action) {

            }
        })
    }

    companion object {
        fun newInstance() =
            SpotifyLoginFragment()
    }
}