package com.example.songmatch.core.domain

import com.example.songmatch.core.data.FirebaseDataSource
import com.example.songmatch.core.domain.model.Room
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.mappers.FirebaseRoomToRoomMapper
import com.example.songmatch.core.models.ResultOf
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

interface RoomRepository {
    suspend fun createRoom(user: User): ResultOf<Int, Unit>
    suspend fun listenToRoom(roomCode: String): Flow<ResultOf<Room, Unit>>
}

class RoomRepositoryImp @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val firebaseRoomToRoomMapper: FirebaseRoomToRoomMapper,
    private val firebaseDataSource: FirebaseDataSource
): RoomRepository {
    override suspend fun createRoom(user: User): ResultOf<Int, Unit> {
        return firebaseDataSource.createRoom(user)
    }

    override suspend fun listenToRoom(roomCode: String): Flow<ResultOf<Room, Unit>> {
        return firebaseDataSource.listenToRoom(roomCode).map {
            when (val firebaseRoom = it.handleResult()) {
                null -> ResultOf.Error(Unit)
                else -> ResultOf.Success(firebaseRoomToRoomMapper.map(firebaseRoom))
            }
        }
    }

}