package com.example.songmatch.core.helpers

import com.example.songmatch.core.framework.retrofit.INVALID_SPOTIFY_TOKEN_MESSAGE
import com.example.songmatch.core.models.ResponseError
import com.example.songmatch.core.models.ResultOf
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.UnknownHostException

//        https://medium.com/nerd-for-tech/safe-retrofit-calls-extension-with-kotlin-coroutines-for-android-in-2021-part-ii-fd55842951cf e https://dev.to/eagskunst/making-safe-api-calls-with-retrofit-and-coroutines-1121
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): ResultOf<T, ResponseError> {
    return withContext(dispatcher) {
        try {
            mapResponse(block())
        } catch (exception: Exception) {
//            TODO: Tratar Erros apontados pelos validators middleware da minha api (Tipo email inválido)
            if (exception is HttpException) {
                mapHttpExceptionToResultError(
                    errorBody = exception.response()?.errorBody(),
                    statusCode = exception.code(),
                    message = exception.message()
                )
            } else {
                mapGenericExceptionToResultError(exception)
            }
        }
    }
}

private fun <T> mapResponse(response: T): ResultOf<T, ResponseError.NetworkError> {
    return if (response !is Response<*>) {
        ResultOf.Success(response)
    } else {
        if (response.isSuccessful) {
            ResultOf.Success(response)
        } else {
            ResultOf.Error(ResponseError.NetworkError())
        }
    }
}

private fun mapHttpExceptionToResultError(
    errorBody: ResponseBody?,
    statusCode: Int,
    message: String,
): ResultOf.Error<ResponseError> = try {
    val errorBodyString = errorBody?.string()
    val errorBodyResponse = if (shouldDeserialize(errorBodyString, statusCode)) {
        deserializeErrorBodyResponse(errorBodyString)
    } else null

    if (errorBodyString == INVALID_SPOTIFY_TOKEN_MESSAGE) {
        ResultOf.Error(
            ResponseError.UnauthorizedError()
        )
    } else {
        ResultOf.Error(
            ResponseError.NetworkError(
                httpCode = statusCode,
                httpMessage = message,
                serverCode = errorBodyResponse?.code,
                serverMessage = errorBodyResponse?.message,
                localizedMessage = errorBodyResponse?.localizedMessage,
                isConnectionError = false
            )
        )
    }
} catch (exception: Exception) {
    mapGenericExceptionToResultError(exception)
}

private fun shouldDeserialize(
    errorBody: String?,
    statusCode: Int,
) = !errorBody.isNullOrEmpty() &&
        statusCode != HttpURLConnection.HTTP_FORBIDDEN &&
        HttpURLConnection.HTTP_INTERNAL_ERROR < statusCode &&
        statusCode < 600


private fun deserializeErrorBodyResponse(errorBody: String?): ErrorBodyResponse? = try {
    MoshiAdapterApiCall.moshiAdapter.fromJson(errorBody!!)
} catch (exception: Exception) {
    null
}

private object MoshiAdapterApiCall {
    val moshiAdapter: JsonAdapter<ErrorBodyResponse> by lazy {
        Moshi.Builder().build().adapter(ErrorBodyResponse::class.java)
    }
}

private fun mapGenericExceptionToResultError(
    exception: Exception,
) = ResultOf.Error(
    ResponseError.NetworkError(
        exceptionTitle = exception::class.simpleName,
        exceptionMessage = exception.message,
        isConnectionError = exception.isConnectionException()
    )
)

internal fun Throwable.isConnectionException(): Boolean =
    this is ConnectException || this is UnknownHostException || this is ConnectionShutdownException

@JsonClass(generateAdapter = true)
data class ErrorBodyResponse(
    @field:Json(name = "code") val code: String? = null,
    @field:Json(name = "message") val message: String? = null,
    @field:Json(name = "localized_message") val localizedMessage: String? = null,
    @field:Json(name = "details") val details: List<String?>? = null,
    @field:Json(name = "key") val key: String? = null,
)