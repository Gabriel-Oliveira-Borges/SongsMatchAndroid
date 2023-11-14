package com.example.songmatch.login.presentation

import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.login.useCase.LoginToSpotifyUseCase
import com.example.songmatch.login.presentation.model.SpotifyLoginViewAction
import com.example.songmatch.login.presentation.model.SpotifyLoginViewAction.RequestLogin
import com.example.songmatch.login.presentation.model.SpotifyLoginViewState
import com.example.songmatch.core.domain.TrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyLoginViewModel @Inject constructor(
    private val loginToSpotifyUseCase: LoginToSpotifyUseCase,
    private val trackRepository: TrackRepository
) : BaseViewModel<SpotifyLoginViewAction, SpotifyLoginViewState>() {
    override val viewState = SpotifyLoginViewState()
    var tracks = mutableListOf<TrackResponse>()

    override fun dispatchViewAction(action: SpotifyLoginViewAction) {
        when (action) {
            is RequestLogin -> requestLogin()
            is SpotifyLoginViewAction.GetUserTracks -> getUserTracks()
        }
    }

    private fun requestLogin() {
        loginToSpotifyUseCase()
    }

    private fun getUserTracks() {
        viewModelScope.launch {
            tracks = trackRepository.updateTracks().handleResult()?.toMutableList() ?: mutableListOf()
            print("Tracks atualizadas")
            print(tracks)
        }
    }
}