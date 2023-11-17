package com.example.songmatch.useCase

import com.example.songmatch.domain.SessionRepository
import com.example.songmatch.domain.TrackRepository
import com.example.songmatch.domain.model.ResultOf
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
                tracksRepository.uploadUserTracks(it)
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