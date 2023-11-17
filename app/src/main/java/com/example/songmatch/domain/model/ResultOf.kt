package com.example.songmatch.domain.model

sealed class ResultOf<out S, out E> {
    data class Success<out S>(val value: S): ResultOf<S, Nothing>()
    data class Error<out E>(val value: E): ResultOf<Nothing, E>()

    inline fun handleResult(
        onSuccess: (result: S) -> Unit = {},
        onError: (error: E) -> Unit = {},
        onFinish: (result: S?) -> Unit = {}
    ): S? = when (this) {
        is Success -> {
            onSuccess(value)
            onFinish(value)
            value
        }
        is Error -> {
            onError(value)
            onFinish(null)
            null
        }
    }

    inline fun <T> mapSuccess(transform: (S) -> T): ResultOf<T, E> = when (this) {
        is Success -> Success(transform(value))
        is Error -> Error(value)
    }

    inline fun <T> mapError(transform: (E) -> T): ResultOf<S, T> = when (this) {
        is Success -> Success(value)
        is Error -> Error(transform(value))
    }

    inline fun onSuccess(block: (S) -> Unit): ResultOf<S, E> {
        if (this is Success) block(value)
        return this
    }

    inline fun onError(block: (E) -> Unit): ResultOf<S, E> {
        if (this is Error) block(value)
        return this
    }

    inline fun onFinish(block: (S?) -> Unit): ResultOf<S, E> {
        val parameter = if (this is Error) null else (this as Success).value
        block(parameter)
        return this
    }

    inline fun <T, F> flatMap(
        transformSuccess: (S) -> ResultOf<T, F>,
        transformError: (E) -> ResultOf<T, F>
    ): ResultOf<T, F> = when (this) {
        is Success -> transformSuccess(value)
        is Error -> transformError(value)
    }
}