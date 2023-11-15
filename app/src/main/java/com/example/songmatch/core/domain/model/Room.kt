package com.example.songmatch.core.domain.model

data class Room(
    val usersToken: List<String>,
    val roomCode: Int,
    val playlistCreated: Boolean,
    val playlistLink: String?
)
