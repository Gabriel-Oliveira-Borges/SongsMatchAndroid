package com.example.songmatch.core.framework.di

import com.example.songmatch.core.api.SpotifyAPI
import com.example.songmatch.core.di.Names
import com.example.songmatch.core.framework.retrofit.AuthInterceptor
import com.example.songmatch.core.useCase.GetCurrentUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

private const val BASE_SPOTIFY_URL = "https://api.spotify.com"
private const val BASE_APP_API_URL = ""

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
    @Named(Names.Retrofit.APP_API)
    fun provideAppApiRetrofit(): Retrofit {
        return provideRetrofit(baseURL = BASE_APP_API_URL)
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor(getCurrentUserUseCase: GetCurrentUserUseCase): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(getCurrentUserUseCase))
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