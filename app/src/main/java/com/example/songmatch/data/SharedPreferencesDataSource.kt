package com.example.songmatch.data

import android.content.Context
import android.util.Log
import com.example.songmatch.extensions.get
import com.example.songmatch.extensions.put
import com.example.songmatch.domain.model.ResultOf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface SharedPreferencesDataSource {
    fun <T> saveValue(key: String, value: T): ResultOf<Unit, Unit>
    fun <T> getValue(key: String, defaultValue: T): ResultOf<T, Unit>
}


private val SHARED_PREFERENCES_NAME = "SONG_MATCH_SHARED_PREFERENCES"

@Suppress("UNCHECKED_CAST")
class SharedPreferencesDataSourceImp @Inject constructor(
    @ApplicationContext private val context: Context
): SharedPreferencesDataSource {
    private val sharedPreferences by lazy { context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE) }

    override fun <T> saveValue(key: String, value: T): ResultOf<Unit, Unit> {
        return when (value) {
            is String -> sharedPreferences.put(key, value as String)
            is Boolean ->sharedPreferences.put(key, value as Boolean)
            is Float -> sharedPreferences.put(key, value as Float)
            is Long -> sharedPreferences.put(key, value as Long)
            else -> {
                Log.e("Error", "It's not possible to save any value of type of $value in sharedPreferences")
                return ResultOf.Error(Unit)
            }
        }
    }

    override fun <T> getValue(key: String, defaultValue: T): ResultOf<T, Unit> {
        return when (defaultValue) {
            is String -> ResultOf.Success(sharedPreferences.get(key, defaultValue as String) as T)
            is Boolean -> ResultOf.Success(sharedPreferences.get(key, defaultValue as Boolean) as T)
            is Float -> ResultOf.Success(sharedPreferences.get(key, defaultValue as Float) as T)
            is Long -> ResultOf.Success(sharedPreferences.get(key, defaultValue as Long) as T)
            else -> {
                Log.e("Error", "It's not possible to save any value of type of ${defaultValue} in sharedPreferences")
                ResultOf.Error(Unit)
            }
        }
    }

}