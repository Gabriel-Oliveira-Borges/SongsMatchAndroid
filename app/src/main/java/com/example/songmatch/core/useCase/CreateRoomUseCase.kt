package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.RoomRepository
import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface CreateRoomUseCase {
    suspend operator fun invoke(): ResultOf<Int, Unit>
}

class CreateRoomUseCaseImp @Inject constructor(
    private val roomRepository: RoomRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val sessionRepository: SessionRepository
): CreateRoomUseCase {
    override suspend fun invoke(): ResultOf<Int, Unit> {
        return getCurrentUserUseCase().handleResult()?.let {
            roomRepository.createRoom(it).onSuccess { roomCode ->
                sessionRepository.updateUserRoom(roomCode.toString())
            }
        } ?: ResultOf.Error(Unit)
    }
}

