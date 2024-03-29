package com.example.songmatch.core.framework.di

import android.content.Context
import com.example.songmatch.core.api.RemoteAPI
import com.example.songmatch.core.api.SpotifyAPI
import com.example.songmatch.core.data.SessionLocalDataSource
import com.example.songmatch.core.di.Names
import com.example.songmatch.core.framework.retrofit.AuthInterceptor
import com.example.songmatch.core.mappers.SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper
import com.example.songmatch.login.useCase.LoginToSpotifyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

private const val BASE_SPOTIFY_URL = "https://api.spotify.com"
private const val BASE_APP_API_URL = "http://10.0.2.2:3000"

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    @Singleton
    @Named(Names.Retrofit.SPOTIFY)
    fun provideSpotifyRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return provideRetrofit(baseURL = BASE_SPOTIFY_URL, okHttpClient = okHttpClient)
    }

    @Provides
    @Singleton
    @Named(Names.Retrofit.REMOTE_API)
    fun provideAppApiRetrofit(): Retrofit {
        return provideRetrofit(baseURL = BASE_APP_API_URL)
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor(
        session: SessionLocalDataSource,
        mapper: SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper,
        loginToSpotifyUseCase: LoginToSpotifyUseCase,
        @ApplicationContext context: Context,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(session, mapper, loginToSpotifyUseCase, context))
            .build()
    }

    @Provides
    fun provideSpotifyAPI(@Named(Names.Retrofit.SPOTIFY) retrofit: Retrofit): SpotifyAPI {
        return retrofit.create(SpotifyAPI::class.java)
    }

//    TODO: ADICIONAR INTERCEPTOR AQUI
    @Provides
    fun provideRemoteAPI(@Named(Names.Retrofit.REMOTE_API) retrofit: Retrofit): RemoteAPI {
        return retrofit.create(RemoteAPI::class.java)
    }

    private fun provideRetrofit(baseURL: String, okHttpClient: OkHttpClient? = null): Retrofit {
        return if (okHttpClient != null) {
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(baseURL)
                .client(okHttpClient)
                .build()
        } else {
            Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(baseURL)
                .build()
        }
    }
}