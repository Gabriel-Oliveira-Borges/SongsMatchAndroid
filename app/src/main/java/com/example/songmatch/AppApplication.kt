package com.example.songmatch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import android.app.Activity
import java.util.*


@HiltAndroidApp
class AppApplication : Application() {
    private var mCurrentActivity: Activity? = null
//    Aceitar uma função que será passada pelo AuthInterceptor e deverá ser executada na MainActivity quando a autenticação for um sucesso
    private var requestInterruptedBySpotifyLogin =  LinkedList<RequestInterruptedBySpotifyLogin>()

    fun getRequestsInterruptedBySpotifyLogin() = requestInterruptedBySpotifyLogin

    fun getCurrentActivity(): Activity? {
        return mCurrentActivity
    }

    fun setCurrentActivity(mCurrentActivity: Activity?) {
        this.mCurrentActivity = mCurrentActivity
    }

    fun enqueueRequestInterruptedBySpotifyLogin(request: RequestInterruptedBySpotifyLogin): Boolean {
        if (!isRequestInQueue(request)) {
            this.requestInterruptedBySpotifyLogin.add(request)
            return true
        }
        return false
    }

    fun dequeueRequestInterruptedBySpotifyLogin(request: RequestInterruptedBySpotifyLogin) {
        this.requestInterruptedBySpotifyLogin.remove(request)
    }

    private fun isRequestInQueue(request: RequestInterruptedBySpotifyLogin): Boolean {
        return this.requestInterruptedBySpotifyLogin.indexOf(request) != -1
    }
}

enum class RequestInterruptedBySpotifyLogin {
    UPDATING_TRACKS
}