package com.example.songmatch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.songmatch.core.useCase.SaveSpotifyUserUseCase
import com.example.songmatch.core.useCase.ShouldUpdateTracksUseCase
import com.example.songmatch.login.presentation.SpotifyLoginFragment
import com.example.songmatch.login.presentation.SPOTIFY_LOGIN_REQUEST_CODE
import com.example.songmatch.login.presentation.model.SpotifyAuthBaseFragment
import com.example.songmatch.main.useCase.UpdateLocalTracksUseCase
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    TODO: Ao entrar em uma sala, criar um websocket para ficar ouvindo as configurações da sala
//     (quais músicas pegar, quem já está na sala, quem já deu upload de todas as suas músicas, se a playlist já foi gerada)

//   TODO: Depois de gerada a playlist, criar um "outro websocket", que irá ficar escutando sobre a música que está sendo reproduzida
//   (Deve ser possível que todos os usuários consigam controlar a música, logo preciso de eventos para play, pause, back e next
//   Como o backend não vai guardar a playlist, enviar o index pra musica ao dar next/back

//    Docs:
//    https://socket.io/docs/v4/server-api/,
//    https://socket.io/blog/native-socket-io-and-android/
//    https://socket.io/blog/socket-io-on-ios/ e https://drive.google.com/drive/u/1/folders/1x3TmfwQtZxM3d60mqhNzHa78jvq3zmsw


    @Inject
    lateinit var saveSpotifyUser: SaveSpotifyUserUseCase
    @Inject
    lateinit var updateLocalTracks: UpdateLocalTracksUseCase

    private lateinit var appApplication: AppApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SpotifyLoginFragment.newInstance())
                .commitNow()

            appApplication = this.applicationContext as AppApplication
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearMainActivityReferencesInApplication()
    }

    override fun onResume() {
        super.onResume()
        appApplication.setCurrentActivity(this)
    }

    override fun onPause() {
        super.onPause()
        clearMainActivityReferencesInApplication()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SPOTIFY_LOGIN_REQUEST_CODE -> {
                val response =
                    AuthenticationClient.getResponse(resultCode, data)
                when (response.type) {
                    AuthenticationResponse.Type.TOKEN -> {
                        lifecycleScope.launch {
                            saveSpotifyUser(
                                token = response.accessToken,
                                expiresIn = response.expiresIn,
                                name = null
                            ).onError {
                                notifyFragments(successful = false)
                            }.onSuccess {
                                resumeInterruptedRequest()
                                notifyFragments(successful = true)
                            }
                        }
                    }
                    AuthenticationResponse.Type.ERROR -> {
                        notifyFragments(successful = false)
                    }
                    else -> {
                        notifyFragments(successful = false)
                    }
                }
            }
        }
    }

    private fun clearMainActivityReferencesInApplication() {
        val currentActivity = appApplication.getCurrentActivity()

        if (this == currentActivity) {
            appApplication.setCurrentActivity(null)
        }
    }

    private fun getAllSpotifyAuthFragment(): List<SpotifyAuthBaseFragment> {
        return supportFragmentManager.fragments.filterIsInstance<SpotifyAuthBaseFragment>()
    }

    private fun notifyFragments(successful: Boolean) {
        getAllSpotifyAuthFragment().forEach {
            if (successful)
                it.onSpotifyLoginSuccess()
            else
                it.onSpotifyLoginError()
        }
    }

    private fun resumeInterruptedRequest() {
        lifecycleScope.launch {
            appApplication.getRequestsInterruptedBySpotifyLogin().forEach {
                when (it) {
                    RequestInterruptedBySpotifyLogin.UPDATING_TRACKS -> updateLocalTracks()
                }
                appApplication.dequeueRequestInterruptedBySpotifyLogin(it)
            }
        }
    }
}