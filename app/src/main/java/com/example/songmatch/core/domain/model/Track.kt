package com.example.songmatch.core.domain.model

import com.example.songmatch.core.api.TimeRange


data class Track(
    val artist: TrackArtist?,
    val uuid: Int,
    val uri: String,
    val timeRange: TimeRange?,
    val duration: String,
    val spotifyId: String,
    val name: String,
    val type: String,
    val popularity: Int,
    val isTopTrack: Boolean,
    val isSavedTrack: Boolean
)



data class TrackArtist(
    val name: String,
    val uri: String,
)
