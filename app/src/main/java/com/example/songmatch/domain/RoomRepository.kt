package com.example.songmatch.domain

import com.example.songmatch.data.FirebaseDataSource
import com.example.songmatch.domain.model.Room
import com.example.songmatch.domain.model.User
import com.example.songmatch.mappers.FirebaseRoomToRoomMapper
import com.example.songmatch.domain.model.ResultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface RoomRepository {
    suspend fun createRoom(user: User): ResultOf<Int, Unit>
    suspend fun getRoom(roomCode: String): ResultOf<Room?, Unit>
    suspend fun listenToRoom(roomCode: String): Flow<ResultOf<Room, Unit>>
    suspend fun isRoomCodeValid(roomCode: String, userToken: String): Boolean
    suspend fun joinRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit>
    suspend fun leaveRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit>
}

class RoomRepositoryImp @Inject constructor(
    private val firebaseRoomToRoomMapper: FirebaseRoomToRoomMapper,
    private val firebaseDataSource: FirebaseDataSource
): RoomRepository {
    override suspend fun createRoom(user: User): ResultOf<Int, Unit> {
        return firebaseDataSource.createRoom(user)
    }

    override suspend fun getRoom(roomCode: String): ResultOf<Room?, Unit> {
        return firebaseDataSource.getRoom(roomCode).mapSuccess {firebaseRoom ->
            firebaseRoom?.let {
                Room(
                    usersToken = it.usersToken,
                    roomCode = it.roomCode,
                    playlistCreated = it.playlistCreated,
                    playlistLink = it.playlistLink,
                )
            }
        }
    }

    override suspend fun leaveRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit> {
        return firebaseDataSource.leaveRoom(roomCode, userToken)
    }

    override suspend fun listenToRoom(roomCode: String): Flow<ResultOf<Room, Unit>> {
        return firebaseDataSource.listenToRoom(roomCode).map {
            when (val firebaseRoom = it.handleResult()) {
                null -> ResultOf.Error(Unit)
                else -> ResultOf.Success(firebaseRoomToRoomMapper.map(firebaseRoom))
            }
        }
    }

    override suspend fun isRoomCodeValid(roomCode: String, userToken: String): Boolean {
        return firebaseDataSource.getRoom(roomCode).handleResult() != null
    }

    override suspend fun joinRoom(roomCode: String, userToken: String): ResultOf<Unit, Unit> {
        return firebaseDataSource.joinRoom(roomCode, userToken)
    }
}