package com.example.songmatch.core.framework.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.framework.room.converters.DateConverters
import com.example.songmatch.core.framework.room.daos.TrackDao
import com.example.songmatch.core.framework.room.entities.TrackEntity
import com.example.songmatch.core.framework.room.entities.UserEntity

@Database(entities = [UserEntity::class, TrackEntity::class], version = 2, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class LocalDataBase: RoomDatabase() {
    abstract fun getUserDao(): UserDao

    abstract fun getTrackDao(): TrackDao
}