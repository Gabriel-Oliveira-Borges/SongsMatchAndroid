package com.example.songmatch.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserEntity(
    @PrimaryKey val spotifyToken: String,
    val spotifyTokenExpiration: Date,
    val remoteToken: String?,
    val email: String?,
    val imageUri: String?,
    val uri: String?,
    val name: String?,
    val lastTrackUpdate: Date?,
    val tracksUploaded: Boolean,
    val currentRoom: String?,
    val id: String
)
