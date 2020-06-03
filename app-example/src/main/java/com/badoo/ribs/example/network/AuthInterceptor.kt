package com.badoo.ribs.example.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Authentication Interceptor
 *
 * Intercepts the [Request] and adds the auth headers to work with the [UnsplashApi]
 */
class AuthInterceptor(accessKey: String) : Interceptor {
    private val authHeaderValue = "Client-ID $accessKey"

    companion object {
        internal const val HEADER_NAME = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(HEADER_NAME, authHeaderValue)
            .build()
        return chain.proceed(request)
    }
}
