package com.example.songmatch.main.domain

import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.data.SessionLocalDataSource
import com.example.songmatch.core.data.SpotifyDataSource
import com.example.songmatch.core.data.TrackDataSource
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface TrackRepository {
    suspend fun updateSongs(): ResultOf<Unit, ResponseError>
}

class TrackRepositoryImp @Inject constructor(
    private val spotifyDataSource: SpotifyDataSource,
    private val trackDataSource: TrackDataSource
): TrackRepository {
    override suspend fun updateSongs(): ResultOf<Unit, ResponseError> {
        val savedTracks = spotifyDataSource.getUserSavedTracks()
            .onError { return ResultOf.Error(ResponseError.NetworkError()) }
            .handleResult()
        val topTracks: List<TrackResponse>? = spotifyDataSource.getUserTopTracks()
            .onError { return ResultOf.Error(ResponseError.NetworkError()) }
            .handleResult()
        val allTracks = savedTracks!! + topTracks!!
        trackDataSource.saveTracks(allTracks)
//        TODO:
//        Colocar hooks de quando o usuario for deletado, todas as musicas dele forem trocadas
        return ResultOf.Success(Unit)
    }
}