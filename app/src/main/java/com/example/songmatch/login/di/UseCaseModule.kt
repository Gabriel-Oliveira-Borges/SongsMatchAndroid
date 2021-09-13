package com.example.songmatch.login.di

import com.example.songmatch.login.useCase.LoginToSpotifyUseCase
import com.example.songmatch.login.useCase.LoginToSpotifyUseCaseImp
import com.example.songmatch.login.useCase.LogoutCurrentUserUseCase
import com.example.songmatch.login.useCase.LogoutCurrentUserUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginUseCaseModule {
    @Binds abstract fun bindLoginToSpotifyUseCase(impl: LoginToSpotifyUseCaseImp): LoginToSpotifyUseCase

    @Binds abstract fun bindLogoutCurrentUserUseCase(impl: LogoutCurrentUserUseCaseImp): LogoutCurrentUserUseCase
}
