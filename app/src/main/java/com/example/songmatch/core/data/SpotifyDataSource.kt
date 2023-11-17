package com.example.songmatch.core.data

import android.util.Log
import com.example.songmatch.core.api.*
import com.example.songmatch.core.framework.room.entities.UserEntity
import com.example.songmatch.core.helpers.getAllPaginatedItems
import com.example.songmatch.core.helpers.safeApiCall
import com.example.songmatch.core.mappers.SpotifyUserResponseToUserMapper
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import java.util.*
import javax.inject.Inject

interface SpotifyDataSource {
    suspend fun getSpotifyUser(
        token: String,
        expiresIn: Date
    ): ResultOf<UserEntity, ResponseError>

    suspend fun getUserSavedTracks(): ResultOf<List<TrackResponse>, ResponseError>
    suspend fun getUserTopTracks(): ResultOf<List<TrackResponse>, ResponseError>

    suspend fun savePlaylistToSpotify(tracksUri: List<String>, userId: String): ResultOf<String?, ResponseError>
}

class SpotifyDataSourceImpl @Inject constructor(
    private val spotifyAPI: SpotifyAPI,
    private val mapper: SpotifyUserResponseToUserMapper
) : SpotifyDataSource {
    override suspend fun getSpotifyUser(
        token: String,
        expiresIn: Date
    ): ResultOf<UserEntity, ResponseError> {
        return safeApiCall { spotifyAPI.getUser() }.mapSuccess {
            mapper.map(from = it, expiresIn = expiresIn, token = token)
        }
    }

    override suspend fun getUserSavedTracks(): ResultOf<List<TrackResponse>, ResponseError> {
        return getAllPaginatedItems(limit = 50) { limit, offset ->
            val response = spotifyAPI.getUserSavedTracks(limit, offset)
            response.items = response.items.map {
                it.track = it.track.copy(isSavedTrack = true)
                it
            }
            response
        }.mapSuccess { it.map { it.track } }
    }

    override suspend fun getUserTopTracks(): ResultOf<List<TrackResponse>, ResponseError> {
        val tracks = mutableListOf<TrackResponse>()

        getTopTrackOFSpecificTimeRange(TimeRange.SHORT_TERM)
            .onError { return ResultOf.Error(it) }
            .onSuccess { tracks.addAll(it) }

        getTopTrackOFSpecificTimeRange(TimeRange.MEDIUM_TERM)
            .onError { return ResultOf.Error(it) }
            .onSuccess { tracks.addAll(it) }

        getTopTrackOFSpecificTimeRange(TimeRange.LONG_TERM)
            .onError { return ResultOf.Error(it) }
            .onSuccess { tracks.addAll(it) }
        return ResultOf.Success(tracks)
    }

    private suspend fun getTopTrackOFSpecificTimeRange(
        timeRange: TimeRange
    ): ResultOf<List<TrackResponse>, ResponseError> {
        return getAllPaginatedItems(limit = 50) { limit, offset ->
            spotifyAPI.getUserTopTracks(
                limit = limit,
                offset = offset,
                timeRange = timeRange.field
            )
        }.onSuccess {
            it.map { track ->
                track.timeRange = timeRange.field
                track.isTopTrack = true
                track
            }
        }.onError {
            Log.e("SPOTIFY-API-ERROR", it.message ?: it.toString())
        }
    }

    override suspend fun savePlaylistToSpotify(tracksUri: List<String>, userId: String): ResultOf<String?, ResponseError> {
        return safeApiCall {
            spotifyAPI.postPlaylist(url = SpotifyRequestPath.postPlaylist(userId), body = PostPlaylistBody(name = "SongMatch"))
        }.mapSuccess {
            for (trackChunk in tracksUri.chunked(100)) {
                spotifyAPI.postPlaylistTracks(
                    url = SpotifyRequestPath.postTracksToPlaylist(it.id),
                    body = PostPlaylistTrackBody(uris = trackChunk)
                )
            }
            it.externalUrls?.spotify
        }
    }
}