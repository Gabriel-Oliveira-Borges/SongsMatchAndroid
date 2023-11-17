package com.example.songmatch.core.t

import com.example.songmatch.core.api.TrackResponse
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.domain.model.TrackArtist
import com.example.songmatch.core.mappers.BaseMapper
import javax.inject.Inject

class TrackResponseToTrackMapper @Inject constructor(

) {
    fun map(from: List<TrackResponse>, userToken: String): List<Track> {
        return from.map {
            Track(
                id = it.id,
                name = it.name,
                popularity = it.popularity,
                timeRange = it.timeRange,
                type = it.type,
                uri = it.uri,
                userToken = userToken,
                albumImageUri = it.album?.images?.firstOrNull()?.url,
                artists = it.artists.map {
                    TrackArtist(
                        id = it.id,
                        name = it.name,
                        uri = it.uri,
                        type = it.type
                    )
                }
            )
        }
    }
}