package com.example.songmatch.di

import com.example.songmatch.useCase.CreatePlaylistInSpotifyUseCase
import com.example.songmatch.useCase.CreatePlaylistInSpotifyUseCaseImp
import com.example.songmatch.useCase.CreatePlaylistUseCase
import com.example.songmatch.useCase.CreatePlaylistUseCaseImp
import com.example.songmatch.useCase.CreateRoomUseCase
import com.example.songmatch.useCase.CreateRoomUseCaseImp
import com.example.songmatch.useCase.GetCurrentUserUseCase
import com.example.songmatch.useCase.GetCurrentUserUseCaseImp
import com.example.songmatch.useCase.GetPlaylistUseCase
import com.example.songmatch.useCase.GetPlaylistUseCaseImp
import com.example.songmatch.useCase.GetTrackDetailsUseCase
import com.example.songmatch.useCase.GetTrackDetailsUseCaseImp
import com.example.songmatch.useCase.JoinRoomUseCase
import com.example.songmatch.useCase.JoinRoomUseCaseImp
import com.example.songmatch.useCase.LeaveRoomUseCase
import com.example.songmatch.useCase.LeaveRoomUseCaseImp
import com.example.songmatch.useCase.ListenToCurrentRoomUseCase
import com.example.songmatch.useCase.ListenToCurrentRoomUseCaseImp
import com.example.songmatch.useCase.LoginToSpotifyUseCase
import com.example.songmatch.useCase.LoginToSpotifyUseCaseImp
import com.example.songmatch.useCase.LogoutCurrentUserUseCase
import com.example.songmatch.useCase.LogoutCurrentUserUseCaseImp
import com.example.songmatch.useCase.SaveSpotifyUserUseCase
import com.example.songmatch.useCase.SaveSpotifyUserUseCaseImp
import com.example.songmatch.useCase.UploadUserTracksUseCase
import com.example.songmatch.useCase.UploadUserTracksUseCaseImp
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
    abstract fun bindJoinRoomUseCaseImp(impl: JoinRoomUseCaseImp): JoinRoomUseCase
    @Binds
    abstract fun bindCreatePlaylistUseCaseImp(impl: CreatePlaylistUseCaseImp): CreatePlaylistUseCase
    @Binds
    abstract fun bindGetPlaylistUseCaseImp(impl: GetPlaylistUseCaseImp): GetPlaylistUseCase
    @Binds
    abstract fun bindGetTrackDetailsUseCaseImp(impl: GetTrackDetailsUseCaseImp): GetTrackDetailsUseCase
    @Binds
    abstract fun bindCreatePlaylistInSpotifyUseCaseImp(impl: CreatePlaylistInSpotifyUseCaseImp): CreatePlaylistInSpotifyUseCase
    @Binds
    abstract fun bindLeaveRoomUseCaseImp(impl: LeaveRoomUseCaseImp): LeaveRoomUseCase
    @Binds
    abstract fun bindGetUserCurrentRoomImp(impl: ListenToCurrentRoomUseCaseImp): ListenToCurrentRoomUseCase

    @Binds abstract fun bindLoginToSpotifyUseCase(impl: LoginToSpotifyUseCaseImp): LoginToSpotifyUseCase

    @Binds abstract fun bindLogoutCurrentUserUseCase(impl: LogoutCurrentUserUseCaseImp): LogoutCurrentUserUseCase
}