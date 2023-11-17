package com.example.songmatch.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.songmatch.domain.model.isTokenExpired
import com.example.songmatch.useCase.GetCurrentUserUseCase
import com.example.songmatch.useCase.LogoutCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class InitViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutCurrentUserUseCase: LogoutCurrentUserUseCase
) : ViewModel() {
    val navigation = MutableLiveData<Navigation>()

    sealed class Navigation {
        object OpenRoomSelectionFlow : Navigation()
        object OpenAuthenticationFlow : Navigation()
    }

    fun onAppStart() {
        this.handleInitialization()
    }

    private fun handleInitialization() {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase().handleResult()

            if (currentUser == null) {
                navigation.postValue(Navigation.OpenAuthenticationFlow)
            } else if (currentUser.isTokenExpired() && !currentUser.tracksUploaded) {
                logoutCurrentUserUseCase()
                navigation.postValue(Navigation.OpenAuthenticationFlow)
            } else if (currentUser.currentRoom != null) {
                navigation.postValue(Navigation.OpenRoomSelectionFlow)
            } else {
                navigation.postValue(Navigation.OpenRoomSelectionFlow)
            }
        }
    }
}