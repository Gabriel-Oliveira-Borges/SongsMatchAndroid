package com.example.songmatch.login.presentation.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.songmatch.login.presentation.listener.SpotifyLoginFragmentListener
import com.example.songmatch.SPOTIFY_TOKEN
import com.example.songmatch.databinding.SpotifyLoginFragmentBinding
import com.example.songmatch.login.presentation.presentation.model.SpotifyLoginViewAction

const val CLIENT_ID = "d9451fbe5bbc4427abf4299070e2dbe1"
const val REDIRECT_URI = "http://localhost:8888/callback"
const val SPOTIFY_LOGIN_REQUEST_CODE = 1337

class SpotifyLoginFragment : Fragment(), SpotifyLoginFragmentListener {
    val sharedPreferences by lazy { requireContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE) }
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
            SpotifyLoginViewAction.RequestLogin(activity = this.requireActivity())
        )
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner, Observer {action ->
            when (action) {

            }
        })
    }

    private fun getFromSharedPreferences() {
        Log.d("MainActivity", sharedPreferences.getString(SPOTIFY_TOKEN, null).toString())
    }

    companion object {
        fun newInstance() =
            SpotifyLoginFragment()
    }
}