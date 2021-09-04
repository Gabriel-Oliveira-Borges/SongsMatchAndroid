package com.example.songmatch.core.di

import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.domain.SessionRepositoryImp
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
    abstract fun bindSpotifyRepository(imp: SessionRepositoryImp): SessionRepository
}