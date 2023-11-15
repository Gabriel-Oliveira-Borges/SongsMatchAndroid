package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.TrackRepository
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface UploadUserTracksUseCase {
    suspend operator fun invoke(): ResultOf<Unit, Unit>
}

class UploadUserTracksUseCaseImp @Inject constructor(
    private val tracksRepository: TrackRepository
): UploadUserTracksUseCase {
    override suspend operator fun invoke(): ResultOf<Unit, Unit> {
        return tracksRepository.uploadUserTracks()
    }
}