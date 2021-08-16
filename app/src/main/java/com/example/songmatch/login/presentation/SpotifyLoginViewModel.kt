package com.example.songmatch.login.presentation

import android.app.Activity
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.LoginToSpotifyUseCase
import com.example.songmatch.login.presentation.model.SpotifyLoginViewAction
import com.example.songmatch.login.presentation.model.SpotifyLoginViewAction.RequestLogin
import com.example.songmatch.login.presentation.model.SpotifyLoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpotifyLoginViewModel @Inject constructor(
    private val loginToSpotifyUseCase: LoginToSpotifyUseCase
): BaseViewModel<SpotifyLoginViewAction, SpotifyLoginViewState>() {
    override val viewState = SpotifyLoginViewState()

    override fun dispatchViewAction(action: SpotifyLoginViewAction) {
        when (action) {
            is RequestLogin -> requestLogin(activity = action.activity)
        }
    }

    private fun requestLogin(activity: Activity) {
        loginToSpotifyUseCase(receiverActivity = activity)
    }
}