package com.example.songmatch.useCase

import com.example.songmatch.domain.RoomRepository
import com.example.songmatch.domain.SessionRepository
import com.example.songmatch.domain.model.ResultOf
import javax.inject.Inject

interface LeaveRoomUseCase {
    suspend operator fun invoke(): ResultOf<Unit, Unit>
}

class LeaveRoomUseCaseImp @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val sessionRepository: SessionRepository,
    private val roomRepository: RoomRepository
): LeaveRoomUseCase {
    override suspend fun invoke(): ResultOf<Unit, Unit> {
        val user = getCurrentUserUseCase().handleResult() ?: return ResultOf.Error(Unit)

        return roomRepository.leaveRoom(roomCode = user.currentRoom!!, userToken = user.spotifyUser.token).onSuccess {
            sessionRepository.updateUserRoom(null)
        }
    }
}