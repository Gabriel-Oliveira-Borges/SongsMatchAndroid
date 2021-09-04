package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface LogoutCurrentUserUseCase {
    suspend operator fun invoke(): ResultOf<Unit, Unit>
}

class LogoutCurrentUserUseCaseImp @Inject constructor(
    private val sessionRepository: SessionRepository
) : LogoutCurrentUserUseCase {
    override suspend fun invoke(): ResultOf<Unit, Unit> {
        return sessionRepository.logoutCurrentUser()
    }
}