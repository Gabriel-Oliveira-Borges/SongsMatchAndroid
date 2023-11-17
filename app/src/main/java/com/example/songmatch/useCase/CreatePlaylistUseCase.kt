package com.example.songmatch.useCase

import com.example.songmatch.domain.TrackRepository
import com.example.songmatch.domain.model.Room
import com.example.songmatch.domain.model.Track
import com.example.songmatch.domain.model.ResultOf
import javax.inject.Inject

interface CreatePlaylistUseCase {
    suspend operator fun invoke(room: Room): ResultOf<Unit, Unit>
}

class CreatePlaylistUseCaseImp @Inject constructor(
    private val tracksRepository: TrackRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): CreatePlaylistUseCase {
    override suspend fun invoke(room: Room): ResultOf<Unit, Unit> {
        val tracks = getAllTracks(room)
        return tracksRepository.savePlaylist(room.roomCode.toString(), tracks.map { it.uri }.shuffled())
    }

    private suspend fun getAllTracks(room: Room): List<Track> {
        val tracks = mutableListOf<Track>()
        val user = getCurrentUserUseCase().handleResult()
        for (userInRoom in room.usersToken) {
            val userTracks = tracksRepository.getUserTracks(user!!).handleResult()
            userTracks?.let { tracks.addAll(it) }
        }
        //TODO: DO THIS LOGIC! Now it is only merging every one's tracks
        return tracks
    }
}