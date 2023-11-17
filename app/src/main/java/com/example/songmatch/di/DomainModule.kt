package com.example.songmatch.di

import com.example.songmatch.domain.RoomRepository
import com.example.songmatch.domain.RoomRepositoryImp
import com.example.songmatch.domain.SessionRepository
import com.example.songmatch.domain.SessionRepositoryImp
import com.example.songmatch.domain.TrackRepository
import com.example.songmatch.domain.TrackRepositoryImp
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