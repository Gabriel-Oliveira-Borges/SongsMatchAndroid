package com.example.songmatch.main.useCase

import com.example.songmatch.core.api.TimeRange
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import com.example.songmatch.core.useCase.ShouldUpdateTracksUseCase
import com.example.songmatch.main.domain.TrackRepository
import javax.inject.Inject

interface GetUserTracksUseCase {
    suspend operator fun invoke(
        topTracks: Boolean? = null,
        savedTracks: Boolean? = null,
        timeRange: TimeRange? = null
    ): ResultOf<List<Track>, ResponseError>
}

class GetUserTracksUseCaseImp @Inject constructor(
    private val shouldUpdateTracksUseCase: ShouldUpdateTracksUseCase,
    private val updateTracksUseCase: UpdateLocalTracksUseCase,
    private val trackRepository: TrackRepository
) : GetUserTracksUseCase {
    override suspend fun invoke(
        topTracks: Boolean?,
        savedTracks: Boolean?,
        timeRange: TimeRange?,
    ): ResultOf<List<Track>, ResponseError> {
        if (shouldUpdateTracksUseCase()) {
            updateTracksUseCase()
        }
        return trackRepository.getSavedTracks()
    }
}