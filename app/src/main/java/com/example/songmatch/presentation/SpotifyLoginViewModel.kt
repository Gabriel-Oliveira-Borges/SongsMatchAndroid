package com.example.songmatch.presentation

import com.example.songmatch.presentation.model.BaseViewModel
import com.example.songmatch.useCase.LoginToSpotifyUseCase
import com.example.songmatch.presentation.model.SpotifyLoginViewAction
import com.example.songmatch.presentation.model.SpotifyLoginViewAction.RequestLogin
import com.example.songmatch.presentation.model.SpotifyLoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpotifyLoginViewModel @Inject constructor(
    private val loginToSpotifyUseCase: LoginToSpotifyUseCase,
) : BaseViewModel<SpotifyLoginViewAction, SpotifyLoginViewState>() {
    override val viewState = SpotifyLoginViewState()

    override fun dispatchViewAction(action: SpotifyLoginViewAction) {
        when (action) {
            is RequestLogin -> requestLogin()
        }
    }

    private fun requestLogin() {
        viewState.isLoading.value = true
        loginToSpotifyUseCase()
    }
}