package com.example.songmatch.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteAPI {
    @POST("/user")
    suspend fun saveUser(@Body body: SaveUserRequestBody): SaveUserRequestResponse
}

@JsonClass(generateAdapter = true)
data class SaveUserRequestBody(
    val name: String,
    val email: String,
    @field:Json(name = "image_uri") val imageUri: String
)

@JsonClass(generateAdapter = true)
data class SaveUserRequestResponse(
    val token: String,
)