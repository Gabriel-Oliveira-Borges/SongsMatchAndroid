package com.example.songmatch.core.mappers

import com.example.songmatch.core.domain.model.SpotifyUser
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.framework.room.entities.UserEntity
import javax.inject.Inject

class UserEntityToUserMapper @Inject constructor() : BaseMapper<UserEntity, User> {
    override fun map(from: UserEntity): User {
        return User(
            name = from.name,
            spotifyUser = SpotifyUser(
                token = from.spotifyToken,
                tokenExpiration = from.spotifyTokenExpiration,
                email = from.email,
                imageUri = from.imageUri,
                uri = from.uri
            ),
            lastTrackUpdate = from.lastTrackUpdate,
            currentRoom = from.currentRoom,
            tracksUploaded = from.tracksUploaded
        )
    }
}