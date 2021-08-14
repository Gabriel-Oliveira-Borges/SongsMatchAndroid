package com.example.songmatch.core.di

import com.example.songmatch.core.data.SharedPreferencesDataSource
import com.example.songmatch.core.data.SharedPreferencesDataSourceImp
import com.example.songmatch.core.data.SpotifyLocalDataSource
import com.example.songmatch.core.data.SpotifyLocalDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindSpotifyLocalDataSource(imp: SpotifyLocalDataSourceImp): SpotifyLocalDataSource

    @Singleton
    @Binds
    abstract fun bindSharedPreferencesDataSource(imp: SharedPreferencesDataSourceImp): SharedPreferencesDataSource
}