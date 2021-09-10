package com.example.songmatch.login.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.LoginToSpotifyUseCase
import com.example.songmatch.login.presentation.model.SpotifyLoginViewAction
import com.example.songmatch.login.presentation.model.SpotifyLoginViewAction.RequestLogin
import com.example.songmatch.login.presentation.model.SpotifyLoginViewState
import com.example.songmatch.main.useCase.GetUserTracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyLoginViewModel @Inject constructor(
    private val loginToSpotifyUseCase: LoginToSpotifyUseCase,
    private val getUserTracksUseCase: GetUserTracksUseCase
) : BaseViewModel<SpotifyLoginViewAction, SpotifyLoginViewState>() {
    override val viewState = SpotifyLoginViewState()

    override fun dispatchViewAction(action: SpotifyLoginViewAction) {
        when (action) {
            is RequestLogin -> requestLogin()
        }
    }

    private fun requestLogin() {
//        loginToSpotifyUseCase()
        viewModelScope.launch {
            val tracks = getUserTracksUseCase().handleResult()
            Log.d("Blah", "${tracks?.size}")
        }
    }
}