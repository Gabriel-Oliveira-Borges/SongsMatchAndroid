package com.example.songmatch.mainMenu.presentation

import com.example.songmatch.core.presentation.BaseViewModel
import com.example.songmatch.mainMenu.presentation.model.RoomViewAction
import com.example.songmatch.mainMenu.presentation.model.RoomViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(

): BaseViewModel<RoomViewAction, RoomViewState>() {
    override val viewState = RoomViewState()

    override fun dispatchViewAction(action: RoomViewAction) {

    }
}