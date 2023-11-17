package com.example.songmatch.useCase

import com.example.songmatch.domain.RoomRepository
import com.example.songmatch.domain.SessionRepository
import com.example.songmatch.domain.model.ResultOf
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

