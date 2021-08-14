package com.example.songmatch.core.domain

import com.example.songmatch.core.data.SpotifyLocalDataSource
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface SpotifyRepository {
    fun getUserToken(): ResultOf<String, Unit>
    fun saveUserToken(token: String, expiresIn: Int): ResultOf<Unit, Unit>
}

class SpotifyRepositoryImp @Inject constructor(
    private val spotifyLocalDataSource: SpotifyLocalDataSource
) : SpotifyRepository {

    override fun getUserToken(): ResultOf<String, Unit> {
        return spotifyLocalDataSource.getUserToken()
    }

    override fun saveUserToken(token: String, expiresIn: Int): ResultOf<Unit, Unit> {
        return spotifyLocalDataSource.saveUserToken(token, expiresIn)
    }
}