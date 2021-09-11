package com.example.songmatch.core.framework.retrofit

import android.content.Context
import com.example.songmatch.AppApplication
import com.example.songmatch.core.data.SessionLocalDataSource
import com.example.songmatch.core.domain.model.isTokenExpired
import com.example.songmatch.core.mappers.SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper
import com.example.songmatch.core.useCase.LoginToSpotifyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody


class AuthInterceptor(
    private val session: SessionLocalDataSource,
    private val spotifyRequestPathToRequestInterruptedBySpotifyLoginMapper: SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper,
    private val loginToSpotifyUseCase: LoginToSpotifyUseCase,
    private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking(Dispatchers.IO) {
            val requestBuilder = chain.request().newBuilder()
            val path =
                requestBuilder.build().url().pathSegments().reduce { acc, s -> "${acc}/${s}" }
            val interruptedRequest =
                spotifyRequestPathToRequestInterruptedBySpotifyLoginMapper.map("/${path}")
            val user = session.getCurrentUser().handleResult()
            val application = (context.applicationContext as AppApplication)

            if (interruptedRequest != null && (user == null || user.isTokenExpired())) {
                if (application.enqueueRequestInterruptedBySpotifyLogin(interruptedRequest)) {
                    loginToSpotifyUseCase()
                }
//                TODO: PENSAR NESSA RESPOSTA E COLOCAR NO mapHttpExceptionToResultError PARA DEVOLVER ResponseError.UnauthorizedError
                return@runBlocking Response.Builder()
                    .code(401) // Whatever code
                    .body(ResponseBody.create(null, "")) // Whatever body
                    .protocol(Protocol.HTTP_2)
                    .message("Dummy response")
                    .request(chain.request())
                    .build()
            } else {
                requestBuilder.addHeader("Authorization", "Bearer ${user?.spotifyUser?.token}")
                return@runBlocking chain.proceed(requestBuilder.build())
            }
        }
    }
}