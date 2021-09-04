package com.example.songmatch.core.domain.model

import java.util.*
import java.util.concurrent.TimeUnit

data class User(
    val name: String?,
    val spotifyUser: SpotifyUser
)

data class SpotifyUser(
    val token: String,
    val tokenExpiration: Date,
    val email: String?,
    val imageUri: String?,
    val uri: String?
)

fun User.firstName() = this.name?.split(" ")?.firstOrNull()


fun User.isTokenExpired(): Boolean {
    val diff = this.spotifyUser.tokenExpiration.time - Date().time
    val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(diff)

    return minutesLeft < 0
}