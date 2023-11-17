package com.example.songmatch.presentation.model

import androidx.lifecycle.MutableLiveData
import com.example.songmatch.domain.model.Room

class RoomViewState {
    var room = MutableLiveData<Room>()
    var subtitle = MutableLiveData<String>()
    var title = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    var action = MutableLiveData<Action>()

    sealed class Action {
        data class OpenPlayerFragment(
            val roomCode: String
        ): Action()

        object GoToMainMenuFragment: Action()
    }
}