package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.domain.TrackRepository
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface UploadUserTracksUseCase {
    suspend operator fun invoke(): ResultOf<Unit, Unit>
}

class UploadUserTracksUseCaseImp @Inject constructor(
    private val tracksRepository: TrackRepository,
    private val sessionRepository: SessionRepository
): UploadUserTracksUseCase {
    override suspend operator fun invoke(): ResultOf<Unit, Unit> {
        //TODO: Extract this logic of verifying if tracks are uploaded to a use case
        val user = sessionRepository.getCurrentUser()
        return user.handleResult()?.let {
            if (!it.tracksUploaded) {
                tracksRepository.uploadUserTracks()
                    .onSuccess {
                        sessionRepository.updateTracksUploaded(true)
                    }
                    .onError {
                        sessionRepository.updateTracksUploaded(false)
                    }
            } else {
                ResultOf.Success(Unit)
            }
        } ?: ResultOf.Error(Unit)
    }
}