package com.example.songmatch.main.di

import com.example.songmatch.main.useCase.GetUserTracksUseCase
import com.example.songmatch.main.useCase.GetUserTracksUseCaseImp
import com.example.songmatch.main.useCase.UpdateLocalTracksUseCase
import com.example.songmatch.main.useCase.UpdateLocalTracksUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindsUpdateLocalSongsUseCase(imp: UpdateLocalTracksUseCaseImp): UpdateLocalTracksUseCase

    @Binds
    abstract fun bindsGetUserTracksUseCase(imp: GetUserTracksUseCaseImp): GetUserTracksUseCase
}