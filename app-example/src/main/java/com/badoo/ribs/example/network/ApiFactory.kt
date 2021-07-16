package com.badoo.ribs.example.network

import com.badoo.ribs.example.auth.AuthStateStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {
    private const val BASE_URL = "https://api.unsplash.com/"
    fun api(
        isDebug: Boolean,
        accessKey: String,
        authStateStorage: AuthStateStorage,
        networkErrorConsumer: Consumer<NetworkError>
    ): UnsplashApi =
        retrofit(
            client = okhttpClient(isDebug, accessKey, authStateStorage, networkErrorConsumer),
            baseUrl = BASE_URL
        ).create(UnsplashApi::class.java)

    private fun okhttpClient(
        isDebug: Boolean,
        accessKey: String,
        authStateStorage: AuthStateStorage,
        networkErrorConsumer: Consumer<NetworkError>
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addNetworkInterceptor(authInterceptor(accessKey, authStateStorage))
            .addNetworkInterceptor(loggingInterceptor(isDebug))
            .addInterceptor(UnauthorizedErrorInterceptor(networkErrorConsumer))
            .build()

    private fun retrofit(client: OkHttpClient, baseUrl: String): Retrofit = Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .baseUrl(baseUrl)
        .build()

    private fun loggingInterceptor(isDebug: Boolean) =
        HttpLoggingInterceptor().apply {
            level = if (isDebug) Level.BODY else Level.NONE
        }

    private fun authInterceptor(
        accessKey: String,
        authStateStorage: AuthStateStorage
    ) = AuthInterceptor(accessKey, authStateStorage)
}
