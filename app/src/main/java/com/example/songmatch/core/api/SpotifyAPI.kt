package com.example.songmatch.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface SpotifyAPI {
    @GET("/v1/me")
    suspend fun getUser(): SpotifyUserResponse

    @GET("/v1/me/tracks")
    suspend fun getUserSavedTracks(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int
    ): PagingObjectResponse<UserSavedTracksResponse>
}

@JsonClass(generateAdapter = true)
data class PagingObjectResponse<T>(
    val href: String,
    val items: List<T>,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int
 ) {
    val hasNext = next != null
    val nextOffset = offset + limit
}

@JsonClass(generateAdapter = true)
data class ArtistObjectResponse(
    val id: String,
    val name: String,
    val uri: String,
    val type: String
)

// GET USER RESPONSE \\
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

// GET USER SAVED TRACKS RESPONSE \\

@JsonClass(generateAdapter = true)
data class UserSavedTracksResponse(
    @field:Json(name = "added_at") val addedAt: String,
    val track: UserSavedTracksTrackResponse
)

@JsonClass(generateAdapter = true)
data class UserSavedTracksTrackResponse(
    val artists: List<ArtistObjectResponse>,
    @field:Json(name = "duration_ms") val duration: String,
    val id: String,
    val name: String,
    val type: String,
    val popularity: Int,
    val uri: String
)