package com.example.songmatch.main.di

import com.example.songmatch.main.domain.TrackRepository
import com.example.songmatch.main.domain.TrackRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindsSongsRepository(imp: TrackRepositoryImp): TrackRepository
}