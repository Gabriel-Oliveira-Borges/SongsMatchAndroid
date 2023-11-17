package com.example.songmatch.domain

import com.example.songmatch.data.api.TrackResponse
import com.example.songmatch.data.FirebaseDataSource
import com.example.songmatch.data.SpotifyDataSource
import com.example.songmatch.domain.model.Playlist
import com.example.songmatch.domain.model.Track
import com.example.songmatch.domain.model.User
import com.example.songmatch.domain.model.ResponseError
import com.example.songmatch.domain.model.ResultOf
import com.example.songmatch.core.t.TrackResponseToTrackMapper
import javax.inject.Inject

interface TrackRepository {
    suspend fun getUserTracks(user: User): ResultOf<List<Track>, ResponseError>
    suspend fun uploadUserTracks(user: User): ResultOf<Unit, Unit>
    suspend fun savePlaylist(roomCode: String, tracksUri: List<String>): ResultOf<Unit, Unit>
    suspend fun getPlaylist(roomCode: String): ResultOf<Playlist, Unit>
    suspend fun getTrackDetails(trackUri: String): ResultOf<Track, Unit>
    suspend fun savePlaylistToSpotify(tracksUri: List<String>, userId: String, roomCode: String): ResultOf<String?, Unit>
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
                        albumImageUri = it.albumImageUri
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

    override suspend fun getTrackDetails(trackUri: String): ResultOf<Track, Unit> {
        return firebaseDataSource.getTrackDetails(trackUri).mapSuccess {
            Track(
                id = it.id,
                name = it.name,
                popularity = it.popularity,
                timeRange = it.timeRange,
                type = it.type,
                uri = it.uri,
                albumImageUri = it.albumImageUri,
                userToken = it.userToken,
                artists = it.artists,
            )
        }
    }

    override suspend fun savePlaylist(roomCode: String, tracksUri: List<String>): ResultOf<Unit, Unit> {
        return firebaseDataSource.createPlaylist(roomCode, tracksUri)
    }

    override suspend fun savePlaylistToSpotify(tracksUri: List<String>, userId: String, roomCode: String): ResultOf<String?, Unit> {
        return spotifyDataSource.savePlaylistToSpotify(tracksUri, userId).mapError {
            Unit
        }.onSuccess {
            if (!it.isNullOrEmpty()) {
                firebaseDataSource.addSpotifyPlaylistUri(roomCode = roomCode, uri = it)
            }
        }
    }
}