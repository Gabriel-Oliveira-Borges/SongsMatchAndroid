package com.example.songmatch.mainMenu.presentation.model

import androidx.lifecycle.MutableLiveData
import com.example.songmatch.core.domain.model.Room

class RoomViewState {
    var room = Room(
        listOf("dasdas", "sadsa"),
        96330,
        playlistCreated = false,
        playlistLink = ""
    )
}