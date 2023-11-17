package com.example.songmatch.useCase

import com.example.songmatch.domain.RoomRepository
import com.example.songmatch.domain.TrackRepository
import com.example.songmatch.domain.model.ResultOf
import javax.inject.Inject

interface CreatePlaylistInSpotifyUseCase {
    suspend operator fun invoke(tracksUri: List<String>, roomCode: String): ResultOf<String?, Unit>
}

class CreatePlaylistInSpotifyUseCaseImp @Inject constructor(
    private val trackRepository: TrackRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val roomRepository: RoomRepository
): CreatePlaylistInSpotifyUseCase {
    override suspend fun invoke(tracksUri: List<String>, roomCode: String): ResultOf<String?, Unit> {

//        TODO: Se o token tiver expirado, tem que autenticar novamente
        getCurrentUserUseCase().handleResult()?.let {user ->
            val room = roomRepository.getRoom(roomCode).handleResult()

            if (!room?.playlistLink.isNullOrEmpty()) {
                return ResultOf.Success(room?.playlistLink)
            } else
                return trackRepository.savePlaylistToSpotify(
                    tracksUri,
                    user.spotifyUser.id,
                    user.currentRoom!!
                )
        } ?: return ResultOf.Error(Unit)
    }
}