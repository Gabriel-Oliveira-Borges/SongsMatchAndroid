package com.example.songmatch.mainMenu.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import com.example.songmatch.mainMenu.presentation.model.RoomSelectionViewAction
import com.example.songmatch.mainMenu.presentation.model.RoomSelectionViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class RoomSelectionViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): BaseViewModel<RoomSelectionViewAction, RoomSelectionViewState>() {
    override val viewState = RoomSelectionViewState()

    override fun dispatchViewAction(action: RoomSelectionViewAction) {
        when (action) {
            is RoomSelectionViewAction.Init -> getCurrentUser()
        }
    }

    fun onCreateRoom() {
        Log.d("RoomSelection","on Create Room")
    }

    fun onJoinRoom() {
        Log.d("RoomSelection","on join Room")
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase().handleResult()

            if (user != null) {
                viewState.greetings.postValue("Olá, ${user.name}")
            }
        }
    }
}