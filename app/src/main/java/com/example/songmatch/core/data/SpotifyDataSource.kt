package com.example.songmatch.core.data

import com.example.songmatch.core.api.SpotifyAPI
import com.example.songmatch.core.framework.room.entities.UserEntity
import com.example.songmatch.core.helpers.safeApiCall
import com.example.songmatch.core.mappers.SpotifyUserResponseToUserMapper
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import java.util.*
import javax.inject.Inject

interface SpotifyDataSource {
    suspend fun getSpotifyUser(token: String, expiresIn: Date): ResultOf<UserEntity, ResponseError.NetworkError>
}

class SpotifyDataSourceImpl @Inject constructor(
    private val spotifyAPI: SpotifyAPI,
    private val mapper: SpotifyUserResponseToUserMapper
) : SpotifyDataSource {
    //        https://medium.com/nerd-for-tech/safe-retrofit-calls-extension-with-kotlin-coroutines-for-android-in-2021-part-ii-fd55842951cf e https://dev.to/eagskunst/making-safe-api-calls-with-retrofit-and-coroutines-1121
    override suspend fun getSpotifyUser(token: String, expiresIn: Date): ResultOf<UserEntity, ResponseError.NetworkError> {
        return safeApiCall { spotifyAPI.getUser() }.mapSuccess {
            mapper.map(from = it, expiresIn = expiresIn, token = token)
        }
    }
}