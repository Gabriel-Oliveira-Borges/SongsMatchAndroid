package com.example.songmatch.core.framework.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.framework.room.converters.DateConverters
import com.example.songmatch.core.framework.room.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class LocalDataBase: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}