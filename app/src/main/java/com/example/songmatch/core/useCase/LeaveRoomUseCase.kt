package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.RoomRepository
import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface LeaveRoomUseCase {
    suspend operator fun invoke(): ResultOf<Unit, Unit>
}

class LeaveRoomUseCaseImp @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val sessionRepository: SessionRepository,
    private val roomRepository: RoomRepository
): LeaveRoomUseCase{
    override suspend fun invoke(): ResultOf<Unit, Unit> {
        val user = getCurrentUserUseCase().handleResult() ?: return ResultOf.Error(Unit)

        return roomRepository.leaveRoom(roomCode = user.currentRoom!!, userToken = user.spotifyUser.token).onSuccess {
            sessionRepository.updateUserRoom(null)
        }
    }
}