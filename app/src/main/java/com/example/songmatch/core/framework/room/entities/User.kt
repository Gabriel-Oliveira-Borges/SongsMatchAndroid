package com.example.songmatch.core.framework.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val token: String,
    val name: String
)

fun User.firstName() = this.name.split(" ").first()
