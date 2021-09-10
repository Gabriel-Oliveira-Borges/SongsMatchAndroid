package com.example.songmatch.core.data

import com.example.songmatch.core.domain.model.User
import com.example.songmatch.core.framework.room.daos.TrackDao
import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.framework.room.entities.UserEntity
import com.example.songmatch.core.mappers.SpotifyUserResponseToUserMapper
import com.example.songmatch.core.mappers.UserEntityToUserMapper
import com.example.songmatch.core.models.ResultOf
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
    suspend fun removeUser(): ResultOf<Unit, Unit>
}

class SessionLocalDataSourceImp @Inject constructor(
    private val userDao: UserDao,
    private val tracksDao: TrackDao,
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
        tracksDao.deleteTracks()
        return ResultOf.Success(userDao.deleteUsers())
    }

    override suspend fun saveUser(
        token: String,
        expiresIn: Date,
        name: String?
    ): ResultOf<Unit, Unit> {
        userDao.deleteUsers()
        val user = UserEntity(
            token = token,
            tokenExpiration = expiresIn,
            email = null,
            imageUri = null,
            uri = null,
            name = name,
            lastTrackUpdate = null
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
                    oldToken = user.token,
                    newToken = token,
                    tokenExpiration = expiresIn
                )
            )
        } ?: ResultOf.Error(Unit)
    }

    override suspend fun removeUser(): ResultOf<Unit, Unit> {
        tracksDao.deleteTracks()
        return ResultOf.Success(userDao.deleteUsers())
    }
}