package com.example.songmatch.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET


interface SpotifyAPI {
    @GET("/v1/me")
    suspend fun getUser(): SpotifyUserResponse
}


@JsonClass(generateAdapter = true)
data class SpotifyUserResponse(
    @Json(name = "display_name") val displayName: String,
    val email: String?,
    val images: List<SpotityUserProfileImageResponse>,
    val uri: String,
    @Json(name = "external_urls")val externalUrls: SpotifyUserExternalUrlsResponse
)

@JsonClass(generateAdapter = true)
data class SpotityUserProfileImageResponse(
    val url: String
)

@JsonClass(generateAdapter = true)
data class SpotifyUserExternalUrlsResponse(
    val spotify: String
)