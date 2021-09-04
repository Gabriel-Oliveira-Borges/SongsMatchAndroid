package com.example.songmatch.main.useCase

import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import com.example.songmatch.main.domain.TrackRepository
import javax.inject.Inject

interface UpdateLocalTracksUseCase {
    suspend operator fun invoke(): ResultOf<Unit, ResponseError>
}

class UpdateLocalTracksUseCaseImp @Inject constructor(
    private val trackRepository: TrackRepository
): UpdateLocalTracksUseCase {
    override suspend fun invoke(): ResultOf<Unit, ResponseError> {
        return trackRepository.updateSongs()
    }
}