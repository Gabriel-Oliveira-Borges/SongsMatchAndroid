package com.example.songmatch.core.di

import com.example.songmatch.core.useCase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetUserSpotifyTokeUseCase(impl: GetCurrentUserUseCaseImp): GetCurrentUserUseCase
    @Binds
    abstract fun bindSaveUserSpotifyTokenUseCase(impl: SaveSpotifyUserUseCaseImp): SaveSpotifyUserUseCase
    @Binds
    abstract fun bindUploadUserTracksUseCase(impl: UploadUserTracksUseCaseImp): UploadUserTracksUseCase
    @Binds
    abstract fun bindCreateRoomUseCaseImp(impl: CreateRoomUseCaseImp): CreateRoomUseCase
    @Binds
    abstract fun bindGetUserCurrentRoomImp(impl: ListenToCurrentRoomUseCaseImp): ListenToCurrentRoomUseCase
}