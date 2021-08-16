package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SpotifyRepository
import javax.inject.Inject

interface SaveSpotifyUserUseCase {
    suspend operator fun invoke(token: String, expiresIn: Int, name: String?)
}

// Todo: Esse token é válido por apenas 1 hora. Quando eu precisar atualizar as músicas, eu vou precisar pedir novamente pelo login. A parte boa é que fica salvo os dados do login
class SaveSpotifyUserUseCaseImp @Inject constructor(
    private val spotifyRepository: SpotifyRepository
): SaveSpotifyUserUseCase {
    override suspend fun invoke(token: String, expiresIn: Int, name: String?) {
        spotifyRepository.saveUser(token, expiresIn, name)
    }
}