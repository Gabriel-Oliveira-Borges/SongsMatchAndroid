package com.example.songmatch.core.domain

import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.data.FirebaseDataSource
import com.example.songmatch.core.data.SpotifyDataSource
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import com.example.songmatch.core.t.TrackResponseToTrackMapper
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import javax.inject.Inject

interface TrackRepository {
    suspend fun getUserTracks(): ResultOf<List<Track>, ResponseError>
    suspend fun uploadUserTracks(): ResultOf<Unit, Unit>
}

class TrackRepositoryImp @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val spotifyDataSource: SpotifyDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val trackResponseToTrackMapper: TrackResponseToTrackMapper

): TrackRepository {

    override suspend fun getUserTracks(): ResultOf<List<Track>, ResponseError> {
        val user = getCurrentUserUseCase().handleResult()
        //TODO: Undo this later.
//        val savedTracks = spotifyDataSource.getUserSavedTracks()
//            .onError { return ResultOf.Error(ResponseError.NetworkError()) }
//            .handleResult()
        val topTracks: List<TrackResponse>? = spotifyDataSource.getUserTopTracks()
            .onError { return ResultOf.Error(ResponseError.NetworkError()) }
            .handleResult()
        val allTracks = /*savedTracks!! +*/ topTracks!!
        return ResultOf.Success(
            trackResponseToTrackMapper.map(
                from = allTracks,
                userToken = user!!.spotifyUser.token
            )
        )
    }

    override suspend fun uploadUserTracks(): ResultOf<Unit, Unit> {
        //TODO: Add retries here!
        val userTracks = this.getUserTracks().handleResult()
        userTracks?.let {
            return firebaseDataSource.addUserTracks(it)
        }
        return ResultOf.Error(Unit)
    }
}