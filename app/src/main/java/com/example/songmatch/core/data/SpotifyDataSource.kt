package com.example.songmatch.core.data

import android.util.Log
import com.example.songmatch.core.api.PagingObjectResponse
import com.example.songmatch.core.api.SpotifyAPI
import com.example.songmatch.core.api.UserSavedTracksResponse
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
    ): ResultOf<UserEntity, ResponseError.NetworkError>

    suspend fun getUserSavedTracks(): ResultOf<List<UserSavedTracksResponse>, ResponseError>
}

class SpotifyDataSourceImpl @Inject constructor(
    private val spotifyAPI: SpotifyAPI,
    private val mapper: SpotifyUserResponseToUserMapper
) : SpotifyDataSource {
    override suspend fun getSpotifyUser(
        token: String,
        expiresIn: Date
    ): ResultOf<UserEntity, ResponseError.NetworkError> {
        return safeApiCall { spotifyAPI.getUser() }.mapSuccess {
            mapper.map(from = it, expiresIn = expiresIn, token = token)
        }
    }

    override suspend fun getUserSavedTracks(): ResultOf<List<UserSavedTracksResponse>, ResponseError> {
        return safeApiCall {
            getAllPaginatedItems(limit = 50) { limit, offset ->
                spotifyAPI.getUserSavedTracks(limit, offset)
            }!!
        }
    }
}