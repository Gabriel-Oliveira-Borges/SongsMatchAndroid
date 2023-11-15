package com.example.songmatch.core.useCase

import com.example.songmatch.core.domain.RoomRepository
import com.example.songmatch.core.models.ResultOf
import javax.inject.Inject

interface GetUserCurrentRoom {
    suspend operator fun invoke(): ResultOf<Int, Unit>
}

class GetUserCurrentRoomImp @Inject constructor(
    private val roomRepository: RoomRepository
): GetUserCurrentRoom {

    override suspend fun invoke(): ResultOf<Int, Unit> {
        // TODO: Get it initially directly from firebase. Update local database later
        return ResultOf.Success(3)
    }
}