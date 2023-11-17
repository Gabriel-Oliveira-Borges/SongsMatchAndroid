package com.example.songmatch.mappers

import com.example.songmatch.domain.model.SpotifyUser
import com.example.songmatch.domain.model.User
import com.example.songmatch.data.room.entities.UserEntity
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
                uri = from.uri,
                id = from.id
            ),
            lastTrackUpdate = from.lastTrackUpdate,
            currentRoom = from.currentRoom,
            tracksUploaded = from.tracksUploaded
        )
    }
}