package com.example.songmatch.core.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.domain.model.isTokenExpired
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class InitViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
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

            if (currentUser?.isTokenExpired() == false) {
                navigation.postValue(Navigation.OpenRoomSelectionFlow)
            } else {
                navigation.postValue(Navigation.OpenAuthenticationFlow)
            }
        }
    }
}