package com.example.songmatch.core.framework.di

import android.content.Context
import androidx.room.Room
import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.framework.room.database.LocalDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Singleton
    @Provides
    fun providesLocalRoomDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, LocalDataBase::class.java, "database-name").build()

    @Provides
    fun provideUserDAO(appDataBase: LocalDataBase): UserDao = appDataBase.getUserDao()
}