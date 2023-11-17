package com.example.songmatch.domain.model

import java.util.*

data class SpotifyUserToken(
    val token: String,
    val expirationDate: Date
) {
    val isExpired: Boolean = false
}
