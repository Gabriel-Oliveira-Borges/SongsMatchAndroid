package com.example.songmatch.core.domain

import com.example.songmatch.core.data.FirebaseDataSource
import com.example.songmatch.core.models.ResultOf
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import javax.inject.Inject

interface RoomRepository {
    suspend fun createRoom(): ResultOf<Int, Unit>
}

class RoomRepositoryImp @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val firebaseDataSource: FirebaseDataSource
): RoomRepository {
    override suspend fun createRoom(): ResultOf<Int, Unit> {
        getCurrentUserUseCase().handleResult()?.let {
            return firebaseDataSource.createRoom(it)
        }
        return ResultOf.Error(Unit)
    }

}