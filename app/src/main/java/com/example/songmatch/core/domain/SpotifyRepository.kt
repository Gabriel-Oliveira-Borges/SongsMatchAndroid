package com.example.songmatch.core.domain

import com.example.songmatch.core.data.SpotifyLocalDataSource
import com.example.songmatch.core.framework.room.entities.User
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface SpotifyRepository {
    suspend fun getCurrentUser(): ResultOf<User?, Unit>
    suspend fun saveUser(token: String, expiresIn: Int, name: String?): ResultOf<Unit, Unit>
}

//TODO: Tirar essas funções daqui e do SpotifyLocalDataSource e mover para SessionRepository e SessionDataSource
class SpotifyRepositoryImp @Inject constructor(
    private val spotifyLocalDataSource: SpotifyLocalDataSource
) : SpotifyRepository {

    override suspend fun getCurrentUser(): ResultOf<User?, Unit> {
        return spotifyLocalDataSource.getCurrentUser()
    }

    override suspend fun saveUser(token: String, expiresIn: Int, name: String?): ResultOf<Unit, Unit> {
        return spotifyLocalDataSource.saveUser(token, expiresIn, name)
    }
}