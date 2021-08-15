package com.example.songmatch.core.framework.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.framework.room.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class LocalDataBase: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}