package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.RoomRepository
import com.example.songmatch.core.domain.model.Room
import com.example.songmatch.core.models.ResultOf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ListenToCurrentRoomUseCase {
    suspend operator fun invoke(): Flow<ResultOf<Room, Unit>>
}

class ListenToCurrentRoomUseCaseImp @Inject constructor(
    private val roomRepository: RoomRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ListenToCurrentRoomUseCase {

    override suspend fun invoke(): Flow<ResultOf<Room, Unit>> {
        // TODO: Remove hardcoded code after current room is in User local database!
        getCurrentUserUseCase().handleResult()?.let {

        }
        return roomRepository.listenToRoom("15842")
    }
}