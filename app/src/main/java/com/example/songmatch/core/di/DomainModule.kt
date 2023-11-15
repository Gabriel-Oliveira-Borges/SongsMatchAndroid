package com.example.songmatch.core.di

import com.example.songmatch.core.domain.RoomRepository
import com.example.songmatch.core.domain.RoomRepositoryImp
import com.example.songmatch.core.domain.SessionRepository
import com.example.songmatch.core.domain.SessionRepositoryImp
import com.example.songmatch.core.domain.TrackRepository
import com.example.songmatch.core.domain.TrackRepositoryImp
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

    @Binds
    @Singleton
    abstract fun bindsSongsRepository(imp: TrackRepositoryImp): TrackRepository

    @Binds
    @Singleton
    abstract fun bindsRoomRepository(imp: RoomRepositoryImp): RoomRepository
}