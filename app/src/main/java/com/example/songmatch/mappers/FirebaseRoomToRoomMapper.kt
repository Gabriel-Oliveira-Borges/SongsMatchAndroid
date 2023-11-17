package com.example.songmatch.mappers

import com.example.songmatch.data.FirebaseDataSource
import com.example.songmatch.domain.model.Room
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