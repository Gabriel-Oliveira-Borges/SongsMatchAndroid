package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SpotifyRepository
import javax.inject.Inject

interface SaveUserSpotifyTokenUseCase {
    operator fun invoke(token: String, expiresIn: Int)
}

// Todo: Esse token é válido por apenas 1 hora. Preciso salvar até quando o token é válido. Quando eu precisar atualizar as músicas, eu vou precisar pedir novamente pelo login. A parte boa é que fica salvo os dados do login
class SaveUserSpotifyTokenUseCaseImp @Inject constructor(
    private val spotifyRepository: SpotifyRepository
): SaveUserSpotifyTokenUseCase {
    override fun invoke(token: String, expiresIn: Int) {
        spotifyRepository.saveUserToken(token, expiresIn)
    }
}