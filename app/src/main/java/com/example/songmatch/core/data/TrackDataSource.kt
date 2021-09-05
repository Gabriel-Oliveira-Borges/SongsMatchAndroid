package com.example.songmatch.core.data

import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.framework.room.daos.TrackDao
import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.mappers.TrackResponseToTrackEntityMapper
import com.example.songmatch.core.models.ResultOf
import java.util.*
import javax.inject.Inject

interface TrackDataSource {
    suspend fun saveTracks(tracks: List<TrackResponse>): ResultOf<Unit, Unit>
}

class TrackDataSourceImp @Inject constructor(
    private val userDao: UserDao,
    private val trackDao: TrackDao,
    private val trackResponseToTrackEntityMapper: TrackResponseToTrackEntityMapper
): TrackDataSource {
    override suspend fun saveTracks(tracks: List<TrackResponse>): ResultOf<Unit, Unit> {
        val trackEntities = trackResponseToTrackEntityMapper.mapList(tracks)
        trackDao.insertTrackList(trackEntities)
        val currentUser = userDao.getCurrentUser()
        currentUser?.let {
            userDao.updateUser(currentUser.copy(lastTrackUpdate = Date()))
        }
        return ResultOf.Success(Unit)
    }
}