package com.example.songmatch.core.data

import com.example.songmatch.core.api.TimeRange
import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.framework.room.daos.TrackDao
import com.example.songmatch.core.framework.room.daos.UserDao
import com.example.songmatch.core.mappers.TrackEntityToTrackMapper
import com.example.songmatch.core.mappers.TrackResponseToTrackEntityMapper
import com.example.songmatch.core.mappers.mapList
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import java.util.*
import javax.inject.Inject

interface TrackDataSource {
    suspend fun getSavedTracks(
        topTracks: Boolean? = null,
        savedTracks: Boolean? = null,
        timeRange: TimeRange? = null
    ): ResultOf<List<Track>, ResponseError>

    suspend fun saveTracks(tracks: List<TrackResponse>): ResultOf<Unit, Unit>
}

class TrackDataSourceImp @Inject constructor(
    private val userDao: UserDao,
    private val trackDao: TrackDao,
    private val trackResponseToTrackEntityMapper: TrackResponseToTrackEntityMapper,
    private val trackEntityToTrackMapper: TrackEntityToTrackMapper
) : TrackDataSource {
    override suspend fun getSavedTracks(
        topTracks: Boolean?,
        savedTracks: Boolean?,
        timeRange: TimeRange?,
    ): ResultOf<List<Track>, ResponseError> {
        return try {
            ResultOf.Success(
                trackEntityToTrackMapper.mapList(
//                    TODO: FAZER TODAS AS 8 POSSIBILIDADES!A
                    if (topTracks != null && savedTracks != null && timeRange != null) {
                        trackDao.getAllTracksWithTopTracksAndTimeRange(
                            timeRange = timeRange.field, topTracks = topTracks
                        )
                    } else if (topTracks != null && savedTracks != null && timeRange == null) {
                        trackDao.getAllTracksWithSavedTracksAndTopTracks(
                            savedTracks = savedTracks, topTracks = topTracks
                        )
                    } else if (topTracks != null && savedTracks == null) {
                        trackDao.getAllTracksWithTopTracks(
                            topTracks = topTracks
                        )
                    } else if (topTracks == null && savedTracks != null && timeRange != null) {
                        trackDao.getAllTracksOfTimeRange(
                            timeRange = timeRange.field,
                        )
                    } else if (topTracks == null && savedTracks != null && timeRange == null) {
                        trackDao.getAllTracksWithSavedTracks(
                            savedTracks = savedTracks
                        )
                    } else if (topTracks == null && savedTracks == null && timeRange != null) {
                        trackDao.getAllTracksOfTimeRange(
                            timeRange = timeRange.field
                        )
                    }
//                    else if (topTracks == null && savedTracks == null && timeRange == null) {
//                        trackDao.getAllTracksWithSavedTracks(
//                            savedTracks = savedTracks
//                        )
//                    }
                     else {
                        trackDao.getAllTracks()
                    }

                )
            )
        } catch (exception: Exception) {
            ResultOf.Error(ResponseError.UnknownError())
        }
    }

    override suspend fun saveTracks(tracks: List<TrackResponse>): ResultOf<Unit, Unit> {
        trackDao.deleteTracks()
        val trackEntities = trackResponseToTrackEntityMapper.mapList(tracks)
        trackDao.insertTrackList(trackEntities)
        userDao.getCurrentUser()?.let {
            userDao.updateUser(it.copy(lastTrackUpdate = Date()))
        }
        return ResultOf.Success(Unit)
    }
}