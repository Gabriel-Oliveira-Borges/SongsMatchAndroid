package com.example.songmatch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import android.app.Activity
import java.util.*


@HiltAndroidApp
class AppApplication : Application() {
    private var mCurrentActivity: Activity? = null

    fun getCurrentActivity(): Activity? {
        return mCurrentActivity
    }

    fun setCurrentActivity(mCurrentActivity: Activity?) {
        this.mCurrentActivity = mCurrentActivity
    }
}

enum class RequestInterruptedBySpotifyLogin {
    UPDATING_TRACKS,
    CREATING_PLAYLIST,
    UPLOADING_TRACKS_TO_PLAYLIST
}