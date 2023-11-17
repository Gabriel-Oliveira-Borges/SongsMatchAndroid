package com.example.songmatch.domain

import com.example.songmatch.data.FirebaseDataSource
import com.example.songmatch.data.SessionLocalDataSource
import com.example.songmatch.data.SpotifyDataSource
import com.example.songmatch.domain.model.User
import com.example.songmatch.domain.model.ResultOf
import java.util.*
import javax.inject.Inject

interface SessionRepository {
    suspend fun getCurrentUser(): ResultOf<User?, Unit>
    suspend fun saveUser(token: String, expiresIn: Date, name: String?): ResultOf<Unit, Unit>
    suspend fun logoutCurrentUser(): ResultOf<Unit, Unit>
    suspend fun updateUserRoom(room: String?): ResultOf<Unit, Unit>
    suspend fun updateTracksUploaded(uploaded: Boolean): ResultOf<Unit, Unit>
}

    class SessionRepositoryImp @Inject constructor(
        private val sessionLocalDataSource: SessionLocalDataSource,
        private val spotifyDataSource: SpotifyDataSource,
        private val firebaseDataSource: FirebaseDataSource
    ) : SessionRepository {
        override suspend fun getCurrentUser(): ResultOf<User?, Unit> {
            return sessionLocalDataSource.getCurrentUser()
        }

        override suspend fun saveUser(token: String, expiresIn: Date, name: String?): ResultOf<Unit, Unit> {
            return sessionLocalDataSource.saveUser(token, expiresIn, name).onSuccess {
                spotifyDataSource.getSpotifyUser(token = token, expiresIn = expiresIn).onSuccess {
                    sessionLocalDataSource.saveUser(userEntity = it)
                    firebaseDataSource.addUser(it)
                }.onError {
                    sessionLocalDataSource.removeUser()
                }
            }
        }

        override suspend fun logoutCurrentUser(): ResultOf<Unit, Unit> {
            val currentUser = this.getCurrentUser().handleResult()
            return sessionLocalDataSource.logoutCurrentUser().mapSuccess {
                currentUser?.let {
                    firebaseDataSource.removeUser(it)
                }
            }
        }

        override suspend fun updateTracksUploaded(uploaded: Boolean): ResultOf<Unit, Unit> {
            return sessionLocalDataSource.updateTracksUploaded(uploaded)
        }

        override suspend fun updateUserRoom(room: String?): ResultOf<Unit, Unit> {
            return sessionLocalDataSource.updateUserRoom(room)
        }
}