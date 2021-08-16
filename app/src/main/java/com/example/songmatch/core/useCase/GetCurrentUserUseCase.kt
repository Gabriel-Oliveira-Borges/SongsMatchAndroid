package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SpotifyRepository
import com.example.songmatch.core.framework.room.entities.User
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

// Todo: Fazer testes de tudo o que for possível testar
interface GetCurrentUserUseCase {
    suspend operator fun invoke(): ResultOf<User?, Unit>
}

class GetCurrentUserUseCaseImp @Inject constructor(
    private val spotifyRepository: SpotifyRepository
) : GetCurrentUserUseCase {
    override suspend operator fun invoke(): ResultOf<User?, Unit> {
        return spotifyRepository.getCurrentUser()
    }
}