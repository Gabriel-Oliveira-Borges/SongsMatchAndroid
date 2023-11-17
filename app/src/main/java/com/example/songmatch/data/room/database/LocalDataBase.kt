package com.example.songmatch.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.songmatch.data.room.daos.UserDao
import com.example.songmatch.data.room.converters.DateConverters
import com.example.songmatch.data.room.entities.UserEntity

@Database(entities = [UserEntity::class], version = 5, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class LocalDataBase: RoomDatabase() {
    abstract fun getUserDao(): UserDao
}