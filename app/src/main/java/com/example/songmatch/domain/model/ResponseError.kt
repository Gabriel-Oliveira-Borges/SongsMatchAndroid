package com.example.songmatch.domain.model

sealed class ResponseError(val message: String?) {
    class NetworkError(
        val httpCode: Int = -1,
        val httpMessage: String? = null,
        val serverCode: String? = null,
        val serverMessage: String? = null,
        val exceptionTitle: String? = null,
        val exceptionMessage: String? = null,
        val localizedMessage: String? = null,
        val isConnectionError: Boolean = false,
    ) : ResponseError(httpMessage)

    class UnavailableNetworkConnectionError : ResponseError("Connection unavailable")
    class UnauthorizedError : ResponseError("User not authorized")
    class UnknownError(val msg: String? = null) : ResponseError(msg ?: "Unknown error")
    class InvalidFieldsError(val fieldIds: List<String>) : ResponseError("Invalid fields ($fieldIds)")
}