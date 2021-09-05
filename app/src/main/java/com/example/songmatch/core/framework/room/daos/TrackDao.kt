package com.example.songmatch.core.framework.room.daos

import androidx.room.*
import com.example.songmatch.core.domain.model.Track
import com.example.songmatch.core.framework.room.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Insert
    suspend fun insertTrackList(userList: List<TrackEntity>)

    @Query("DELETE FROM TrackEntity")
    suspend fun deleteTracks()

    @Query("SELECT * FROM TrackEntity")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity WHERE timeRange == :timeRange")
    suspend fun getAllTracksOfTimeRange(timeRange: String): List<TrackEntity>

    @Query("SELECT * FROM TrackEntity")
    fun observeTracks(): Flow<TrackEntity?>
}