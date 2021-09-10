package com.example.songmatch.core.di

import com.example.songmatch.core.useCase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

//Todo: Study more about the hilt's components (https://developer.android.com/training/dependency-injection/hilt-android#generated-components)
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds abstract fun bindLoginToSpotifyUseCase(impl: LoginToSpotifyUseCaseImp): LoginToSpotifyUseCase

    @Binds abstract fun bindGetUserSpotifyTokeUseCase(impl: GetCurrentUserUseCaseImp): GetCurrentUserUseCase

    @Binds abstract fun bindSaveUserSpotifyTokenUseCase(impl: SaveSpotifyUserUseCaseImp): SaveSpotifyUserUseCase

    @Binds abstract fun bindLogoutCurrentUserUseCase(impl: LogoutCurrentUserUseCaseImp): LogoutCurrentUserUseCase

    @Binds abstract fun bindShouldUpdateTracksUseCase(impl: ShouldUpdateTracksUseCaseImp): ShouldUpdateTracksUseCase
}
