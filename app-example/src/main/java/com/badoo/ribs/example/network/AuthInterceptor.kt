package com.badoo.ribs.example.network

import com.badoo.ribs.example.auth.AuthState
import com.badoo.ribs.example.auth.AuthStateStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Authentication Interceptor
 *
 * Intercepts the [Request] and adds the auth headers to work with the [UnsplashApi]
 */
class AuthInterceptor(accessKey: String, private val authTokenProvider: AuthStateStorage) :
    Interceptor {
    private val authHeaderValue = "Client-ID $accessKey"

    companion object {
        internal const val HEADER_NAME = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val authHeaderValue = when (val authState = authTokenProvider.state) {
            is AuthState.Authenticated -> "Bearer ${authState.accessToken}"
            else -> authHeaderValue
        }
        val request = chain.request()
            .newBuilder()
            .addHeader(
                HEADER_NAME,
                authHeaderValue
            )
            .build()
        return chain.proceed(request)
    }
}
