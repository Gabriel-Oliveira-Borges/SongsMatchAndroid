package com.example.songmatch.core.data

import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.framework.room.entities.User
import com.example.songmatch.core.models.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface SpotifyLocalDataSource {
    suspend fun getCurrentUser(): ResultOf<User?, Unit>
    suspend fun saveUser(token: String, expiresIn: Int, name: String?): ResultOf<Unit, Unit>
    suspend fun updateUserName(name: String): ResultOf<Unit, Unit>
    suspend fun updateToken(token: String, expiresIn: Int): ResultOf<Unit, Unit>
    suspend fun removeUser(): ResultOf<Unit, Unit>
}

class SpotifyLocalDataSourceImp @Inject constructor(
    private val userDao: UserDao,
): SpotifyLocalDataSource {
    override suspend fun getCurrentUser(): ResultOf<User?, Unit> {
        return withContext(Dispatchers.IO) {
            return@withContext ResultOf.Success(userDao.getCurrentUser())
        }
    }

    override suspend fun saveUser(token: String, expiresIn: Int, name: String?): ResultOf<Unit, Unit> {
        val tokenExpiration = calculateTokenExpiration(expiresIn)
        val user = User(token = token, name = name, tokenExpiration = tokenExpiration)
        userDao.insertUser(user)
        return ResultOf.Success(Unit)
    }

    override suspend fun updateUserName(name: String): ResultOf<Unit, Unit> {
        return userDao.getCurrentUser()?.let {
            return ResultOf.Success(userDao.updateUser(it.copy(name = name)))
        } ?: ResultOf.Error(Unit)
    }

    override suspend fun updateToken(token: String, expiresIn: Int): ResultOf<Unit, Unit> {
        val user = userDao.getCurrentUser()
        val tokenExpiration = calculateTokenExpiration(expiresIn)
        return user?.let {
            ResultOf.Success(
                userDao.updateUserToken(oldToken = user.token, newToken = token, tokenExpiration = tokenExpiration)
            )
        } ?: ResultOf.Error(Unit)
    }

    override suspend fun removeUser(): ResultOf<Unit, Unit> {
        val user = userDao.getCurrentUser()

        return user?.let {
            ResultOf.Success(userDao.deleteUser(user))
        } ?: ResultOf.Error(Unit)
    }

    private fun calculateTokenExpiration(expiresIn: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, expiresIn)
        return calendar.time
    }
}