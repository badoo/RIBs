package com.badoo.ribs.example.network

import io.reactivex.functions.Consumer
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Unauthorized Interceptor
 *
 * Intercepts the [Response] and notifies network errors consumer if code == 401
 */
class UnauthorizedErrorInterceptor(
    private val consumer: Consumer<NetworkError>
) : Interceptor {

    @Suppress("MagicNumber")
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request())
            .also { response ->
                if (response.code() == 401) {
                    consumer.accept(NetworkError.Unauthorized)
                }
            }

}
