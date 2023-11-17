package com.example.songmatch.core.domain

import android.util.Log
import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.data.FirebaseDataSource
import com.example.songmatch.core.data.SpotifyDataSource
import com.example.songmatch.core.domain.model.Playlist
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.domain.model.TrackArtist
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import com.example.songmatch.core.t.TrackResponseToTrackMapper
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import javax.inject.Inject

interface TrackRepository {
    suspend fun getUserTracks(user: User): ResultOf<List<Track>, ResponseError>
    suspend fun uploadUserTracks(user: User): ResultOf<Unit, Unit>
    suspend fun savePlaylist(roomCode: String, tracksUri: List<String>): ResultOf<Unit, Unit>
    suspend fun getPlaylist(roomCode: String): ResultOf<Playlist, Unit>
}

class TrackRepositoryImp @Inject constructor(
    private val spotifyDataSource: SpotifyDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val trackResponseToTrackMapper: TrackResponseToTrackMapper

): TrackRepository {

    override suspend fun getUserTracks(user: User): ResultOf<List<Track>, ResponseError> {
        if (user.tracksUploaded) {

            return firebaseDataSource.getUserTracks(user.spotifyUser.token).mapSuccess { tracks ->
                //TODO: Extract this into a mapper
                tracks.map {
                    Track(
                        id = it.id,
                        name = it.name,
                        popularity = it.popularity,
                        timeRange = it.timeRange,
                        type = it.type,
                        uri = it.uri,
                        userToken = it.userToken,
                        artists = it.artists,
                    )
                }
            }.mapError {
                ResponseError.NetworkError()
            }
        } else {

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
                    userToken = user.spotifyUser.token
                )
            )
        }
    }

    override suspend fun uploadUserTracks(user: User): ResultOf<Unit, Unit> {
        //TODO: Add retries here!
        val userTracks = this.getUserTracks(user).handleResult()
        userTracks?.let {
            return firebaseDataSource.addUserTracks(it)
        }
        return ResultOf.Error(Unit)
    }

    override suspend fun getPlaylist(roomCode: String): ResultOf<Playlist, Unit> {
        return firebaseDataSource.getPlaylist(roomCode).mapSuccess { playlist ->
            //TODO: Extract this into a mapper
            Playlist(
                roomCode = playlist.roomCode,
                tracksUri = playlist.tracksUri,
            )
        }
    }

    override suspend fun savePlaylist(roomCode: String, tracksUri: List<String>): ResultOf<Unit, Unit> {
        return firebaseDataSource.createPlaylist(roomCode, tracksUri)
    }
}