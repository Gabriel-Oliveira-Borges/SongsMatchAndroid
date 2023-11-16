package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.RoomRepository
import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface JoinRoomUseCase {
    suspend operator fun invoke(roomCode: String): ResultOf<Unit, String>
}

class JoinRoomUseCaseImp @Inject constructor(
    private val roomRepository: RoomRepository,
    private val sessionRepository: SessionRepository
): JoinRoomUseCase {
    override suspend fun invoke(roomCode: String): ResultOf<Unit, String> {
        val user = sessionRepository.getCurrentUser().handleResult() ?: return ResultOf.Error("Usuário não encontrado")

        return if (roomRepository.isRoomCodeValid(roomCode, user.spotifyUser.token)) {
            roomRepository.joinRoom(roomCode, userToken = user.spotifyUser.token).mapError {
                "Erro ao entrar na sala"
            }
        } else {
            ResultOf.Error("Room code invalid")
        }
    }
}