package com.example.songmatch.core.framework.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val uuid: Int = 0,
    val uri: String,
    val artistUri: String?,
    val artistName: String?,
    var timeRange: String? = null,
    val duration: String,
    val spotifyId: String,
    val name: String,
    val type: String,
    val popularity: Int,
    val isTopTrack: Boolean,
    val isSavedTrack: Boolean
)