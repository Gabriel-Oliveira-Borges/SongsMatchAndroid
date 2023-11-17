package com.example.songmatch.extensions

import android.content.SharedPreferences
import com.example.songmatch.domain.model.ResultOf

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    when(T::class) {
        Boolean::class -> return this.getBoolean(key, defaultValue as Boolean) as T
        Float::class -> return this.getFloat(key, defaultValue as Float) as T
        Int::class -> return this.getInt(key, defaultValue as Int) as T
        Long::class -> return this.getLong(key, defaultValue as Long) as T
        String::class -> return this.getString(key, defaultValue as String) as T
        else -> {
            if (defaultValue is Set<*>) {
                return this.getStringSet(key, defaultValue as Set<String>) as T
            }
        }
    }

    return defaultValue
}

inline fun <reified T> SharedPreferences.put(key: String, value: T): ResultOf<Unit, Unit> {
    with(this.edit()) {
        when(T::class) {
            Boolean::class -> putBoolean(key, value as Boolean)
            Float::class -> putFloat(key, value as Float)
            Int::class -> putInt(key, value as Int)
            Long::class -> putLong(key, value as Long)
            String::class -> putString(key, value as String)
            else -> {
                if (value is Set<*>) {
                    putStringSet(key, value as Set<String>)
                } else {
                    return ResultOf.Error(Unit)
                }
            }
        }
        apply()
    }
    return ResultOf.Success(Unit)
}