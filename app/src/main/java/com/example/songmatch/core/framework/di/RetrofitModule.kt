package com.example.songmatch.core.framework.di

import android.content.Context
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton


private const val BASE_SPOTIFY_URL = "https://api.spotify.com"

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
    fun providesAuthInterceptor(
        session: SessionLocalDataSource,
        mapper: SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper,
        loginToSpotifyUseCase: LoginToSpotifyUseCase,
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(session, mapper, loginToSpotifyUseCase, context))
            .addInterceptor(loggingInterceptor  )
            .build()
    }

    @Provides
    fun provideSpotifyAPI(@Named(Names.Retrofit.SPOTIFY) retrofit: Retrofit): SpotifyAPI {
        return retrofit.create(SpotifyAPI::class.java)
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