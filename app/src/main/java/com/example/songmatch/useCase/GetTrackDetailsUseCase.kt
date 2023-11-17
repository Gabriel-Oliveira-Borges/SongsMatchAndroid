package com.example.songmatch.useCase

import com.example.songmatch.domain.TrackRepository
import com.example.songmatch.domain.model.Track
import com.example.songmatch.domain.model.ResultOf
import javax.inject.Inject

interface GetTrackDetailsUseCase {
    suspend operator fun invoke(trackUri: String): ResultOf<Track, Unit>
}

class GetTrackDetailsUseCaseImp @Inject constructor(
    private val trackRepository: TrackRepository
): GetTrackDetailsUseCase {
    override suspend fun invoke(trackUri: String): ResultOf<Track, Unit> {
        return trackRepository.getTrackDetails(trackUri)
    }
}