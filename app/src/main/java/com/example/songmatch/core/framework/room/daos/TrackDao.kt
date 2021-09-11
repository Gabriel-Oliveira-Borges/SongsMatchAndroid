package com.example.songmatch.core.framework.room.daos

import androidx.room.*
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.framework.room.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Insert
    suspend fun insertTrackList(userList: List<TrackEntity>)

    @Query("DELETE FROM TrackEntity")
    suspend fun deleteTracks()

    @Query("SELECT * FROM TrackEntity")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity WHERE timeRange == :timeRange")
    suspend fun getAllTracksOfTimeRange(timeRange: String): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity WHERE isTopTrack == :topTracks")
    suspend fun getAllTracksWithTopTracks(topTracks: Boolean): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity WHERE isSavedTrack == :savedTracks")
    suspend fun getAllTracksWithSavedTracks(savedTracks: Boolean): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity WHERE isSavedTrack == :savedTracks AND isTopTrack == :topTracks")
    suspend fun getAllTracksWithSavedTracksAndTopTracks(savedTracks: Boolean, topTracks: Boolean): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity WHERE timeRange == :timeRange AND isTopTrack == :topTracks")
    suspend fun getAllTracksWithTopTracksAndTimeRange(timeRange: String, topTracks: Boolean): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity")
    fun observeTracks(): Flow<TrackEntity?>
}