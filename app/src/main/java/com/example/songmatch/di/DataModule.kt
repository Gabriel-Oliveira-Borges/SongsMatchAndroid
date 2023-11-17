package com.example.songmatch.di

import com.example.songmatch.data.FirebaseDataSource
import com.example.songmatch.data.FirebaseDataSourceImp
import com.example.songmatch.data.SessionLocalDataSource
import com.example.songmatch.data.SessionLocalDataSourceImp
import com.example.songmatch.data.SharedPreferencesDataSource
import com.example.songmatch.data.SharedPreferencesDataSourceImp
import com.example.songmatch.data.SpotifyDataSource
import com.example.songmatch.data.SpotifyDataSourceImpl
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
    abstract fun bindFirebaseDataSource(imp: FirebaseDataSourceImp): FirebaseDataSource
}