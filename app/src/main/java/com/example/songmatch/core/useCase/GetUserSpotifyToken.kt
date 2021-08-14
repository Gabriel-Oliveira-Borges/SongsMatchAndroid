package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SpotifyRepository
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

// Todo: Fazer testes de tudo o que for possível testar
interface GetUserSpotifyToken {
    operator fun invoke(): ResultOf<String, Unit>
}

class GetUserSpotifyTokenImp @Inject constructor(
    private val spotifyRepository: SpotifyRepository
) : GetUserSpotifyToken {
    override operator fun invoke(): ResultOf<String, Unit> {
        return spotifyRepository.getUserToken()
    }
}