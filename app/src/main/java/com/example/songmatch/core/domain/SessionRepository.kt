package com.example.songmatch.core.domain

import com.example.songmatch.core.data.SessionLocalDataSource
import com.example.songmatch.core.data.SpotifyDataSource
import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.models.ResultOf
import java.util.*
import javax.inject.Inject

interface SessionRepository {
    suspend fun getCurrentUser(): ResultOf<User?, Unit>
    suspend fun saveUser(token: String, expiresIn: Date, name: String?): ResultOf<Unit, Unit>
    suspend fun logoutCurrentUser(): ResultOf<Unit, Unit>
}

class SessionRepositoryImp @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val spotifyDataSource: SpotifyDataSource,
) : SessionRepository {

    override suspend fun getCurrentUser(): ResultOf<User?, Unit> {
        return sessionLocalDataSource.getCurrentUser()
    }

    override suspend fun saveUser(token: String, expiresIn: Date, name: String?): ResultOf<Unit, Unit> {
        return sessionLocalDataSource.saveUser(token, expiresIn, name).onSuccess {
            spotifyDataSource.getSpotifyUser(token = token, expiresIn = expiresIn).onSuccess {
                sessionLocalDataSource.saveUser(userEntity = it)
            }.onError {
                sessionLocalDataSource.removeUser()
            }
        }
    }

    override suspend fun logoutCurrentUser(): ResultOf<Unit, Unit> {
        return sessionLocalDataSource.logoutCurrentUser()
    }
}