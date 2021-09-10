package com.example.songmatch.core.useCase

import javax.inject.Inject

interface ShouldUpdateTracksUseCase {
    suspend operator fun invoke(): Boolean
}

class ShouldUpdateTracksUseCaseImp @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ShouldUpdateTracksUseCase {
    override suspend fun invoke(): Boolean {
        return getCurrentUserUseCase().handleResult()?.shouldUpdateTracks == true
    }
}