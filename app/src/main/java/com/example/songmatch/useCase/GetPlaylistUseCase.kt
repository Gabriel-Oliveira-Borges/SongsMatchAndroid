package com.example.songmatch.useCase

import com.example.songmatch.domain.TrackRepository
import com.example.songmatch.domain.model.Playlist
import com.example.songmatch.domain.model.ResultOf
import javax.inject.Inject

interface GetPlaylistUseCase {
    suspend operator fun invoke(roomCode: String): ResultOf<Playlist, Unit>
}

class GetPlaylistUseCaseImp @Inject constructor(
    private val trackRepository: TrackRepository
): GetPlaylistUseCase {
    override suspend fun invoke(roomCode: String): ResultOf<Playlist, Unit> {
        return trackRepository.getPlaylist(roomCode)
    }
}