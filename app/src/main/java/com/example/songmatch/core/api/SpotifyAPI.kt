package com.example.songmatch.core.api

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET


interface SpotifyAPI {
    @GET("/v1/me")
    suspend fun getUser(): Response<UserProfile>
}


// TODO: Renomear essas classes para "AlgumaCoisaResponse"
@JsonClass(generateAdapter = true)
data class UserProfile(
    @Json(name = "display_name") val displayName: String,
    val email: String?,
    val images: List<UserProfileImage>,
    val uri: String,
    @Json(name = "external_urls")val externalUrls: UserProfileExternalUrls
)

@JsonClass(generateAdapter = true)
data class UserProfileImage(
    val url: String
)

@JsonClass(generateAdapter = true)
data class UserProfileExternalUrls(
    val spotify: String
)