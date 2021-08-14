package com.example.songmatch.core.models

import java.util.*

data class SpotifyUserToken(
    val token: String,
    val expirationDate: Date
) {
    val isExpired: Boolean = false
}
