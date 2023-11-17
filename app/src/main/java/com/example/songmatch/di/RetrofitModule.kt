package com.example.songmatch.di

import android.content.Context
import com.example.songmatch.data.api.SpotifyAPI
import com.example.songmatch.data.SessionLocalDataSource
import com.example.songmatch.network.AuthInterceptor
import com.example.songmatch.mappers.SpotifyRequestPathToRequestInterruptedBySpotifyLoginMapper
import com.example.songmatch.useCase.LoginToSpotifyUseCase
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



@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    private val BASE_SPOTIFY_URL = "https://api.spotify.com"
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