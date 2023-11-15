package com.example.songmatch.core.mappers

import com.example.songmatch.core.data.FirebaseDataSource
import com.example.songmatch.core.domain.model.Room
import javax.inject.Inject

class FirebaseRoomToRoomMapper @Inject constructor(

) : BaseMapper<FirebaseDataSource.FirebaseRoom, Room> {
    override fun map(from: FirebaseDataSource.FirebaseRoom): Room {
        return Room(
            usersToken = from.usersToken,
            roomCode = from.roomCode,
            playlistCreated = from.playlistCreated,
            playlistLink = from.playlistLink,
        )
    }
}