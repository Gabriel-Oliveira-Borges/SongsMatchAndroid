package com.example.songmatch.core.framework.room.daos

import androidx.room.*
import com.example.songmatch.core.framework.room.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>
}