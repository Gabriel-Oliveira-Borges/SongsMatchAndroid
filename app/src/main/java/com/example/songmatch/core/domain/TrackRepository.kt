package com.example.songmatch.core.domain

import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.data.SpotifyDataSource
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface TrackRepository {
    suspend fun updateTracks(): ResultOf<List<TrackResponse>, ResponseError>
}

class TrackRepositoryImp @Inject constructor(
    private val spotifyDataSource: SpotifyDataSource,
): TrackRepository {
    //TODO: Rename it and change success response type!
    override suspend fun updateTracks(): ResultOf<List<TrackResponse>, ResponseError> {
        val savedTracks = spotifyDataSource.getUserSavedTracks()
            .onError { return ResultOf.Error(ResponseError.NetworkError()) }
            .handleResult()
        val topTracks: List<TrackResponse>? = spotifyDataSource.getUserTopTracks()
            .onError { return ResultOf.Error(ResponseError.NetworkError()) }
            .handleResult()
        val allTracks = savedTracks!! + topTracks!!
        return ResultOf.Success(allTracks)
    }
}