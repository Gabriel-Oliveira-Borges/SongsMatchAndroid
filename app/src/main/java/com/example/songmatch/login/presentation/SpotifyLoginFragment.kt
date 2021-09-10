package com.example.songmatch.login.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.songmatch.login.presentation.listener.SpotifyLoginFragmentListener
import com.example.songmatch.databinding.SpotifyLoginFragmentBinding
import com.example.songmatch.login.presentation.model.SpotifyAuthBaseFragment
import com.example.songmatch.login.presentation.model.SpotifyLoginViewAction
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
        // TODO: Essa fragment está como inicial no nav graph. Fazer como eu fiz no BillsManager, criando um fragment inicial, que irá escolher qual fluxo de navegação o app deve seguir com base no accessToken salvo (Ver por quanto tempo ele é válido)
        return SpotifyLoginFragmentBinding.inflate(inflater).apply {
            binding = this
            viewModel = this@SpotifyLoginFragment.viewModel
            listener = this@SpotifyLoginFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }


    override fun onLoginPressed() {
        viewModel.dispatchViewAction(
            SpotifyLoginViewAction.RequestLogin
        )
    }

    override fun onSpotifyLoginError() {
        Log.d("Blah", "Erro ao fazer login")
    }

    override fun onSpotifyLoginSuccess() {

        Log.d("Blah", "Sucesso ao fazer login")
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