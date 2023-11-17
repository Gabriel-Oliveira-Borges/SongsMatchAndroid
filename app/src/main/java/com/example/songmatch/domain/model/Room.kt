package com.example.songmatch.domain.model

data class Room(
    val usersToken: List<String>,
    val roomCode: String,
    val playlistCreated: Boolean,
    val playlistLink: String?
)
