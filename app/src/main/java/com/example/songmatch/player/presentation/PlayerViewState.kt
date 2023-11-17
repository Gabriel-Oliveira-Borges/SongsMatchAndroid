package com.example.songmatch.player.presentation


import androidx.lifecycle.MutableLiveData
import com.example.songmatch.core.domain.model.Track

class PlayerViewState {
    val currentTrack = MutableLiveData<Track>()
    val spotifyTrack = MutableLiveData<com.spotify.protocol.types.Track>()
    var tracksUri = emptyList<String>()
    var trackIndex: Int = 0
        set(value) {
            val nextIndex = if (value < 0) {
                tracksUri.size - 1
            } else if (value >= tracksUri.size) {
                0
            } else {
                value
            }
            field = nextIndex
        }
    var isPlaying = MutableLiveData<Boolean>()
}