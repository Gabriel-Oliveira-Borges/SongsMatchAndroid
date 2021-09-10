package com.example.songmatch.core.mappers

import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.framework.room.entities.TrackEntity
import javax.inject.Inject

class TrackResponseToTrackEntityMapper @Inject constructor(

): BaseMapper<TrackResponse, TrackEntity> {
    override fun map(from: TrackResponse): TrackEntity {
        val artist = from.artists.firstOrNull()
        return TrackEntity(
            uri = from.uri,
            artistUri = artist?.uri,
            artistName = artist?.name,
            timeRange = from.timeRange,
            duration = from.duration,
            spotifyId = from.id,
            name = from.name,
            type = from.type,
            popularity = from.popularity,
            isTopTrack = from.isTopTrack,
            isSavedTrack = from.isSavedTrack
        )
    }
}