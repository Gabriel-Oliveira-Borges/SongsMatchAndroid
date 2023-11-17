package com.example.songmatch.data

import com.example.songmatch.domain.model.User
import com.example.songmatch.data.room.daos.UserDao
import com.example.songmatch.data.room.entities.UserEntity
import com.example.songmatch.mappers.UserEntityToUserMapper
import com.example.songmatch.domain.model.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface SessionLocalDataSource {
    suspend fun getCurrentUser(): ResultOf<User?, Unit>
    suspend fun logoutCurrentUser(): ResultOf<Unit, Unit>
    suspend fun saveUser(token: String, expiresIn: Date, name: String?): ResultOf<Unit, Unit>
    suspend fun saveUser(userEntity: UserEntity): ResultOf<Unit, Unit>
    suspend fun updateUserName(name: String): ResultOf<Unit, Unit>
    suspend fun updateToken(token: String, expiresIn: Date): ResultOf<Unit, Unit>
    suspend fun updateUserRoom(room: String?): ResultOf<Unit, Unit>
    suspend fun updateTracksUploaded(uploaded: Boolean): ResultOf<Unit, Unit>
    suspend fun removeUser(): ResultOf<Unit, Unit>
}

class SessionLocalDataSourceImp @Inject constructor(
    private val userDao: UserDao,
    private val userEntityToUserMapper: UserEntityToUserMapper
) : SessionLocalDataSource {
    override suspend fun getCurrentUser(): ResultOf<User?, Unit> {
        return withContext(Dispatchers.IO) {
            val userEntity = userDao.getCurrentUser()

            return@withContext userEntity?.let {
                ResultOf.Success(userEntityToUserMapper.map(it))
            } ?: ResultOf.Success(null)
        }
    }

    override suspend fun logoutCurrentUser(): ResultOf<Unit, Unit> {
        return ResultOf.Success(userDao.deleteUsers())
    }

    override suspend fun saveUser(
        token: String,
        expiresIn: Date,
        name: String?
    ): ResultOf<Unit, Unit> {
        userDao.deleteUsers()
        val user = UserEntity(
            spotifyToken = token,
            spotifyTokenExpiration = expiresIn,
            email = null,
            imageUri = null,
            uri = null,
            name = name,
            lastTrackUpdate = null,
            remoteToken = null,
            tracksUploaded = false,
            currentRoom = null,
            id = ""
        )
        userDao.insertUser(user)
        return ResultOf.Success(Unit)
    }

    override suspend fun saveUser(userEntity: UserEntity): ResultOf<Unit, Unit> {
        userDao.deleteUsers()
        userDao.insertUser(userEntity)
        return ResultOf.Success(Unit)
    }

    override suspend fun updateUserName(name: String): ResultOf<Unit, Unit> {
        return userDao.getCurrentUser()?.let {
            return ResultOf.Success(userDao.updateUser(it.copy(name = name)))
        } ?: ResultOf.Error(Unit)
    }

    override suspend fun updateToken(token: String, expiresIn: Date): ResultOf<Unit, Unit> {
        val user = userDao.getCurrentUser()
        return user?.let {
            ResultOf.Success(
                userDao.updateUserToken(
                    oldToken = user.spotifyToken,
                    newToken = token,
                    tokenExpiration = expiresIn
                )
            )
        } ?: ResultOf.Error(Unit)
    }

    override suspend fun updateTracksUploaded(uploaded: Boolean): ResultOf<Unit, Unit> {
        val user = userDao.getCurrentUser()?.copy(
            tracksUploaded = uploaded
        )
        return user?.let {
            ResultOf.Success(
                userDao.updateUser(it)
            )
        } ?: ResultOf.Error(Unit)
    }

    override suspend fun updateUserRoom(room: String?): ResultOf<Unit, Unit> {
        val user = userDao.getCurrentUser()?.copy(
            currentRoom = room
        )
        return user?.let {
            ResultOf.Success(
                userDao.updateUser(it)
            )
        } ?: ResultOf.Error(Unit)
    }

    override suspend fun removeUser(): ResultOf<Unit, Unit> {
        return ResultOf.Success(userDao.deleteUsers())
    }
}