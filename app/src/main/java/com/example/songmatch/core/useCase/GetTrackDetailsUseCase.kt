package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.TrackRepository
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface GetTrackDetailsUseCase {
    suspend operator fun invoke(trackUri: String): ResultOf<Track, Unit>
}

class GetTrackDetailsUseCaseImp @Inject constructor(
    private val trackRepository: TrackRepository
):  GetTrackDetailsUseCase {
    override suspend fun invoke(trackUri: String): ResultOf<Track, Unit> {
        return trackRepository.getTrackDetails(trackUri)
    }
}