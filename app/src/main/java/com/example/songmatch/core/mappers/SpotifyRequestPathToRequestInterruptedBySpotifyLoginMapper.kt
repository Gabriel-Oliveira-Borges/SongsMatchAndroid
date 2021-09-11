package com.example.songmatch.core.mappers

import com.example.songmatch.RequestInterruptedBySpotifyLogin
import com.example.songmatch.core.api.SpotifyRequestPath
import javax.inject.Inject

class SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper @Inject constructor(

): BaseMapper<String, RequestInterruptedBySpotifyLogin?> {
    override fun map(from: String): RequestInterruptedBySpotifyLogin? {
        return when (from) {
            SpotifyRequestPath.getSavedTracks -> RequestInterruptedBySpotifyLogin.UPDATING_TRACKS
            SpotifyRequestPath.getTopTracks -> RequestInterruptedBySpotifyLogin.UPDATING_TRACKS
            else -> null
        }
    }
}