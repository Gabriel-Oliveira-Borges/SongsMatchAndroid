package com.example.songmatch.domain.model

import java.util.*
import java.util.concurrent.TimeUnit

data class User(
    val name: String?,
    val spotifyUser: SpotifyUser,
    val lastTrackUpdate: Date?,
    val tracksUploaded: Boolean,
    val currentRoom: String?
) {
    val shouldUpdateTracks: Boolean get() {
        if (this.lastTrackUpdate == null)
            return true
        val diff = Date().time - this.lastTrackUpdate.time
        val daysPassed = TimeUnit.MILLISECONDS.toDays(diff)

        return daysPassed > 7
    }
}

data class SpotifyUser(
    val token: String,
    val tokenExpiration: Date,
    val email: String?,
    val imageUri: String?,
    val uri: String?,
    val id: String
)

fun User.firstName() = this.name?.split(" ")?.firstOrNull()


fun User.isTokenExpired(): Boolean {
    val diff = this.spotifyUser.tokenExpiration.time - Date().time
    val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(diff)

    return minutesLeft < 0
}