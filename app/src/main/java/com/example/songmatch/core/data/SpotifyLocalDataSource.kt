package com.example.songmatch.core.data

import android.content.Context
import com.example.songmatch.core.extensions.get
import com.example.songmatch.core.models.ResultOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

interface SpotifyLocalDataSource {
    fun getUserToken(): ResultOf<String, Unit>
    fun saveUserToken(token: String, expiresIn: Int): ResultOf<Unit, Unit>
    fun updateToken(token: String, expiresIn: Int): ResultOf<Unit, Unit>
    fun removeToken(): ResultOf<Unit, Unit>
}

private val SPOTIFY_USER_TOKEN_KEY = "SPOTIFY_USER_TOKEN_KEY"
private val SPOTIFY_USER_TOKEN_EXPIRATION_KEY = "SPOTIFY_USER_TOKEN_EXPIRATION_KEY"
private val SPOTIFY_USER_TOKEN_EXPIRATION_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss"

class SpotifyLocalDataSourceImp @Inject constructor(
    private val sharedPreferencesDataSource: SharedPreferencesDataSource,
    @ApplicationContext private val context: Context
): SpotifyLocalDataSource {
    override fun getUserToken(): ResultOf<String, Unit> {
        //        TODO: DEVOLVER NAO APENAS O TOKEN, MAS SIM O `SpotifyUserToken`, COM TODOS OS SEUS DADOS
        val token: String? = sharedPreferencesDataSource.getValue(SPOTIFY_USER_TOKEN_KEY, "").handleResult()
//        TODO: OS DADOS NÃO ESTÃO SENDO PEGOS CORRETAMENTE. SALVAR ESTÁ ROLANDO!
        return when {
            !token.isNullOrEmpty() -> ResultOf.Success(token)
            else -> ResultOf.Error(Unit)
        }

    }

    override fun saveUserToken(token: String, expiresIn: Int): ResultOf<Unit, Unit> {
        return sharedPreferencesDataSource.saveValue(key = SPOTIFY_USER_TOKEN_KEY, value = token).onSuccess {
            val dateFormat = SimpleDateFormat(SPOTIFY_USER_TOKEN_EXPIRATION_DATE_FORMAT);

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, expiresIn)

            val date = dateFormat.format(calendar.time);

            sharedPreferencesDataSource.saveValue(key = SPOTIFY_USER_TOKEN_EXPIRATION_KEY, value = date)
        }
    }

    override fun updateToken(token: String, expiresIn: Int): ResultOf<Unit, Unit> {
        TODO("Not yet implemented")
    }

    override fun removeToken(): ResultOf<Unit, Unit> {
        TODO("Not yet implemented")
    }
}