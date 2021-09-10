package com.example.songmatch.core.mappers

interface BaseMapper<S, R> {
    fun map(from: S): R
}

fun <S, R> BaseMapper<S, R>.mapList(from: List<S>): List<R> {
    return from.map { this.map(it) }
}