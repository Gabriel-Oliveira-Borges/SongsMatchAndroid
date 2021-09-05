package com.example.songmatch.core.framework.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserEntity(
    @PrimaryKey val token: String,
    val tokenExpiration: Date,
    val email: String?,
    val imageUri: String?,
    val uri: String?,
    val name: String?,
    val lastTrackUpdate: Date?,
)
