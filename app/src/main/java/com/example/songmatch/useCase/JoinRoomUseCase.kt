package com.example.songmatch.useCase

import com.example.songmatch.domain.RoomRepository
import com.example.songmatch.domain.SessionRepository
import com.example.songmatch.domain.model.ResultOf
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

        return if (user.currentRoom != null) {
            ResultOf.Success(Unit)
        } else if (roomRepository.isRoomCodeValid(roomCode, user.spotifyUser.token)) {
            roomRepository.joinRoom(roomCode, userToken = user.spotifyUser.token)
                .onSuccess {
                    sessionRepository.updateUserRoom(roomCode)
                }
                .mapError {
                    "Erro ao entrar na sala"
                }
        } else {
            ResultOf.Error("Room code invalid")
        }
    }
}