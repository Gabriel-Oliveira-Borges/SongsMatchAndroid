package com.example.songmatch.core.framework.retrofit

import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking(Dispatchers.IO) {
            val requestBuilder = chain.request().newBuilder()
            val user = getCurrentUserUseCase().handleResult()

            user?.let {
                requestBuilder.addHeader("Authorization", "Bearer ${it.token}")
            }
            return@runBlocking chain.proceed(requestBuilder.build())
        }
    }
}