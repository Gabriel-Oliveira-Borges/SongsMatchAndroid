package com.example.songmatch.main.useCase

import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import com.example.songmatch.core.useCase.ShouldUpdateTracksUseCase
import com.example.songmatch.main.domain.TrackRepository
import javax.inject.Inject

interface UpdateLocalTracksUseCase {
    suspend operator fun invoke(): ResultOf<Unit, ResponseError>
}

class UpdateLocalTracksUseCaseImp @Inject constructor(
    private val trackRepository: TrackRepository,
    private val shouldUpdateTracksUseCase: ShouldUpdateTracksUseCase,
) : UpdateLocalTracksUseCase {
    override suspend fun invoke(): ResultOf<Unit, ResponseError> {
        if (shouldUpdateTracksUseCase()) {
            var triesLeft = 3
            var successful = false
            do {
                trackRepository.updateTracks()
                    .onError { triesLeft-- }
                    .onSuccess { successful = true }
            } while (triesLeft > 0 && !successful)
            if (!successful)
                return ResultOf.Error(ResponseError.UnknownError())
        }
        return ResultOf.Success(Unit)
    }
}