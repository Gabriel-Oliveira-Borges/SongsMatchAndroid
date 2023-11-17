package com.example.songmatch.mappers

import com.example.songmatch.data.api.SpotifyUserResponse
import com.example.songmatch.data.room.entities.UserEntity
import java.util.*
import javax.inject.Inject

class SpotifyUserResponseToUserMapper @Inject constructor() {
    fun map(from: SpotifyUserResponse, expiresIn: Date, token: String): UserEntity {
        return UserEntity(
            name = from.displayName,
            spotifyTokenExpiration = expiresIn,
            spotifyToken = token,
            email = from.email,
            imageUri = from.images.firstOrNull()?.url,
            uri = from.uri,
            lastTrackUpdate = null,
            remoteToken = null,
            currentRoom = null,
            tracksUploaded = false,
            id = from.id
        )
    }
}