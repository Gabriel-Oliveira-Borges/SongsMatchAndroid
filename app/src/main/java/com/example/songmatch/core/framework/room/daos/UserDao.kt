package com.example.songmatch.core.framework.room.daos

import androidx.room.*
import com.example.songmatch.core.framework.room.entities.User
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM User")
    suspend fun deleteUsers()

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getCurrentUser(): User?

    @Query("SELECT * FROM user LIMIT 1")
    fun observeUser(): Flow<User?>

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE user SET token = :newToken, tokenExpiration = :tokenExpiration WHERE token = :oldToken")
    suspend fun updateUserToken(oldToken: String, newToken: String, tokenExpiration: Date)
}