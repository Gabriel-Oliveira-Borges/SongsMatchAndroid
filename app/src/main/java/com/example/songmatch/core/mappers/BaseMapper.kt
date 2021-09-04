package com.example.songmatch.core.mappers

interface BaseMapper<S, R> {
    fun map(from: S): R
}