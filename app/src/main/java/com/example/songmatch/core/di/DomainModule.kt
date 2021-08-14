package com.example.songmatch.core.di

import com.example.songmatch.core.domain.SpotifyRepository
import com.example.songmatch.core.domain.SpotifyRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Singleton
    @Binds
    abstract fun bindSpotifyRepository(imp: SpotifyRepositoryImp): SpotifyRepository
}