package com.example.songmatch.core.framework.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import java.util.concurrent.TimeUnit

@Entity
data class User(
    @PrimaryKey val token: String,
    val name: String?,
    val tokenExpiration: Date
)

fun User.firstName() = this.name?.split(" ")?.firstOrNull()


fun User.isTokenExpired(): Boolean {
    val diff = this.tokenExpiration.time - Date().time
    val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(diff)

    return minutesLeft < 0
}