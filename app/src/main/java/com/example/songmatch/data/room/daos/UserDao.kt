package com.example.songmatch.data.room.daos

import androidx.room.*
import com.example.songmatch.data.room.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)

    @Query("DELETE FROM UserEntity")
    suspend fun deleteUsers()

    @Query("SELECT * FROM UserEntity LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT * FROM UserEntity LIMIT 1")
    fun observeUser(): Flow<UserEntity?>

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("UPDATE UserEntity SET spotifyToken = :newToken, spotifyTokenExpiration = :tokenExpiration WHERE spotifyToken = :oldToken")
    suspend fun updateUserToken(oldToken: String, newToken: String, tokenExpiration: Date)
}