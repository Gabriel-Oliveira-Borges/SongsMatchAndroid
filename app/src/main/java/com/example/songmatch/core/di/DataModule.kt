package com.example.songmatch.core.di

import com.example.songmatch.core.data.*
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
    abstract fun bindSpotifyLocalDataSource(imp: SessionLocalDataSourceImp): SessionLocalDataSource

    @Singleton
    @Binds
    abstract fun bindSharedPreferencesDataSource(imp: SharedPreferencesDataSourceImp): SharedPreferencesDataSource

    @Singleton
    @Binds
    abstract fun bindSpotifyDataSource(imp: SpotifyDataSourceImpl): SpotifyDataSource

    @Singleton
    @Binds
    abstract fun bindTrackDataSource(imp: TrackDataSourceImp): TrackDataSource
}