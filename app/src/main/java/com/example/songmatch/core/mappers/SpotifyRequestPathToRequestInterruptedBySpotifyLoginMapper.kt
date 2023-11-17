package com.example.songmatch.core.mappers

import com.example.songmatch.RequestInterruptedBySpotifyLogin
import com.example.songmatch.core.api.SpotifyRequestPath
import javax.inject.Inject

class SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper @Inject constructor(

): BaseMapper<String, RequestInterruptedBySpotifyLogin?> {
    override fun map(from: String): RequestInterruptedBySpotifyLogin? {
        return if (from == SpotifyRequestPath.getSavedTracks || from == SpotifyRequestPath.getTopTracks) {
            RequestInterruptedBySpotifyLogin.UPDATING_TRACKS
        } else if (from.startsWith("https://api.spotify.com/v1/users/")) {
            RequestInterruptedBySpotifyLogin.CREATING_PLAYLIST
        } else if (from.startsWith("https://api.spotify.com/v1/playlists/")) {
            RequestInterruptedBySpotifyLogin.UPLOADING_TRACKS_TO_PLAYLIST
        } else {
            null
        }
    }
}