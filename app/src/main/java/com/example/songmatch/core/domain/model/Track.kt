package com.example.songmatch.core.domain.model

import com.example.songmatch.core.api.TimeRange


data class Track(
    val id: String,
    val name: String,
    val popularity: Int,
    val timeRange: String?,
    val type: String,
    val uri: String,
    val albumImageUri: String?,
    val userToken: String,
    val artists: List<TrackArtist>,
)



data class TrackArtist(
    val id: String,
    val name: String,
    val uri: String,
    val type: String
)
