package com.example.songmatch.presentation.model

import com.spotify.android.appremote.api.SpotifyAppRemote

sealed class PlayerViewAction {
    data class SetSpotifyAppRemote(
        val mSpotifyAppRemote: SpotifyAppRemote
    ): PlayerViewAction()

    data class OnInit(
        val roomCode: String
    ): PlayerViewAction()
}