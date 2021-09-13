package com.example.songmatch.core.mappers

import com.example.songmatch.core.api.SpotifyUserResponse
import com.example.songmatch.core.framework.room.entities.UserEntity
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
            lastTrackUpdate = null
        )
    }
}