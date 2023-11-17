package com.example.songmatch.useCase

import android.util.Log
import com.example.songmatch.domain.RoomRepository
import com.example.songmatch.domain.model.Room
import com.example.songmatch.domain.model.ResultOf
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ListenToCurrentRoomUseCase {
    suspend operator fun invoke(): Flow<ResultOf<Room, Unit>>?
}

class ListenToCurrentRoomUseCaseImp @Inject constructor(
    private val roomRepository: RoomRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
): ListenToCurrentRoomUseCase {

    override suspend fun invoke(): Flow<ResultOf<Room, Unit>>? {
        val user = getCurrentUserUseCase().handleResult()
        Log.d("ListenTonCurrentRoom", user.toString())
        return user?.currentRoom?.let { roomRepository.listenToRoom(it) }
    }
}