package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

// Todo: Fazer testes de tudo o que for possível testar
interface GetCurrentUserUseCase {
    suspend operator fun invoke(): ResultOf<User?, Unit>
}

class GetCurrentUserUseCaseImp @Inject constructor(
    private val sessionRepository: SessionRepository
) : GetCurrentUserUseCase {
    override suspend operator fun invoke(): ResultOf<User?, Unit> {
        return sessionRepository.getCurrentUser()
    }
}