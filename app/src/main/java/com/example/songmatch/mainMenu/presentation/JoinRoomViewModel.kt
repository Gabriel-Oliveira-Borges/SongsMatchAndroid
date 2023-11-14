package com.example.songmatch.mainMenu.presentation

import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.mainMenu.presentation.model.JoinRoomViewAction
import com.example.songmatch.mainMenu.presentation.model.JoinRoomViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel()
class JoinRoomViewModel @Inject constructor(

) : BaseViewModel<JoinRoomViewAction, JoinRoomViewState>() {
    override val viewState = JoinRoomViewState()

    override fun dispatchViewAction(action: JoinRoomViewAction) {

    }

    fun onJoinRoomPressed() {

    }
}