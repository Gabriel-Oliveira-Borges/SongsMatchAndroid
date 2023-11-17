package com.example.songmatch.mainMenu.presentation.model

import androidx.lifecycle.MutableLiveData
import com.example.songmatch.core.domain.model.Room

class RoomViewState {
    var room = MutableLiveData<Room>()
    var subtitle = MutableLiveData<String>()
    var title = MutableLiveData<String>()

    var action = MutableLiveData<Action>()

    sealed class Action {
        data class OpenPlayerFragment(
            val roomCode: String
        ): Action()
    }
}