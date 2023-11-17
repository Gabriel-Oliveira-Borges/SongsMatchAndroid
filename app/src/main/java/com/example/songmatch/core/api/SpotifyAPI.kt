package com.example.songmatch.core.api

import com.example.songmatch.RequestInterruptedBySpotifyLogin
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url


enum class TimeRange(val field: String) {
    SHORT_TERM("short_term"),
    MEDIUM_TERM("medium_term"),
    LONG_TERM("long_term");

    companion object {
        fun fromString(value: String?): TimeRange? {
            return TimeRange.values().firstOrNull { it.field == value }
        }
    }
}

object SpotifyRequestPath {
    const val getUser = "/v1/me"
    const val getSavedTracks = "/v1/me/tracks"
    const val getTopTracks = "/v1/me/top/tracks"

    fun postPlaylist(userId: String): String = "https://api.spotify.com/v1/users/${userId}/playlists"

    fun postTracksToPlaylist(playlistId: String) = "https://api.spotify.com/v1/playlists/${playlistId}/tracks"
}

interface SpotifyAPI {
    @GET(SpotifyRequestPath.getUser)
    suspend fun getUser(): SpotifyUserResponse

    @POST()
    suspend fun postPlaylist(@Url url: String, @Body body: PostPlaylistBody): PostPlaylistResponse

    @POST()
    suspend fun postPlaylistTracks(@Url url: String, @Body body: PostPlaylistTrackBody): Any

    
    @GET(SpotifyRequestPath.getSavedTracks)
    suspend fun getUserSavedTracks(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int
    ): PagingObjectResponse<UserSavedTracksResponse>

    @GET(SpotifyRequestPath.getTopTracks)
    suspend fun getUserTopTracks(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int,
        @Query("time_range") timeRange: String
    ): PagingObjectResponse<TrackResponse>
}

abstract class RequiresSpotifyToken{
    abstract var requestId: RequestInterruptedBySpotifyLogin
}


@JsonClass(generateAdapter = true)
data class PagingObjectResponse<T>(
    val href: String,
    var items: List<T>,
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
    @Json(name = "external_urls") val externalUrls: SpotifyUserExternalUrlsResponse,
    val id: String
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
    override var requestId: RequestInterruptedBySpotifyLogin = RequestInterruptedBySpotifyLogin.UPDATING_TRACKS,
    @field:Json(name = "added_at") val addedAt: String,
    var track: TrackResponse,
): RequiresSpotifyToken()

@JsonClass(generateAdapter = true)
data class TrackResponse(
    val artists: List<ArtistObjectResponse>,
    @field:Json(name = "duration_ms") val duration: String,
    val id: String,
    val name: String,
    val type: String,
    val popularity: Int,
    val uri: String,
    val album: TrackAlbumResponse?,
    var timeRange: String? = null,
    var isSavedTrack: Boolean = false,
    var isTopTrack: Boolean = false,
)

@JsonClass(generateAdapter = true)
data class TrackAlbumResponse(
    val images: List<TrackAlbumImagesResponse>
)

@JsonClass(generateAdapter = true)
data class TrackAlbumImagesResponse(
    val url: String
)

@JsonClass(generateAdapter = true)
data class PostPlaylistBody(
    val name: String,
    val description: String ="Playlist criada por songmatch"
)

@JsonClass(generateAdapter = true)
data class PostPlaylistResponse(
    val id: String,
    val uri: String,
    @field:Json(name = "external_urls") val externalUrls: PostPlaylistExternalUrlsResponse?
)

@JsonClass(generateAdapter = true)
data class PostPlaylistExternalUrlsResponse(
    val spotify: String
)

@JsonClass(generateAdapter = true)
data class PostPlaylistTrackBody(
    val uris: List<String>,
)