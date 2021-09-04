package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.models.ResultOf
import java.util.*
import javax.inject.Inject

interface SaveSpotifyUserUseCase {
    suspend operator fun invoke(token: String, expiresIn: Int, name: String?): ResultOf<Unit, Unit>
}

// Todo: Esse token é válido por apenas 1 hora. Quando eu precisar atualizar as músicas, eu vou precisar pedir novamente pelo login. A parte boa é que fica salvo os dados do login
class SaveSpotifyUserUseCaseImp @Inject constructor(
    private val sessionRepository: SessionRepository
): SaveSpotifyUserUseCase {
    override suspend fun invoke(token: String, expiresIn: Int, name: String?): ResultOf<Unit, Unit> {
        return sessionRepository.saveUser(token, calculateTokenExpiration(expiresIn), name)
    }

    private fun calculateTokenExpiration(expiresIn: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, expiresIn)
        return calendar.time
    }
}