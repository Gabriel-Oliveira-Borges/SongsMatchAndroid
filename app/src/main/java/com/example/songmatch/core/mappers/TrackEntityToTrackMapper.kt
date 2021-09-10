package com.example.songmatch.core.mappers

import com.example.songmatch.core.api.TimeRange
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.domain.model.TrackArtist
import com.example.songmatch.core.framework.room.entities.TrackEntity
import javax.inject.Inject

class TrackEntityToTrackMapper @Inject constructor(

) : BaseMapper<TrackEntity, Track> {
    override fun map(from: TrackEntity): Track {
        var trackArtist: TrackArtist? = null
        if (from.artistName != null && from.artistUri != null) {
            trackArtist = TrackArtist(name = from.artistName, uri = from.artistUri)
        }
        return Track(
            artist = trackArtist,
            uuid = from.uuid,
            uri = from.uri,
            timeRange = TimeRange.fromString(from.timeRange),
            duration = from.duration,
            spotifyId = from.spotifyId,
            name = from.name,
            type = from.type,
            popularity = from.popularity,
            isTopTrack = from.isTopTrack,
            isSavedTrack = from.isSavedTrack
        )
    }
}