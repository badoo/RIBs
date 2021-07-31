package com.badoo.ribs.example.auth

import com.badoo.ribs.example.BuildConfig
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.network.model.AccessToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers

interface AuthDataSource : AuthStateStorage {
    fun login(authCode: String): Single<AccessToken>
    fun loginAnonymous()
    fun logout()
}

class AuthDataSourceImpl(
    private val api: UnsplashApi,
    storage: AuthStateStorage,
    private val accessKey: String = BuildConfig.ACCESS_KEY,
    private val clientSecret: String = BuildConfig.CLIENT_SECRET
) : AuthDataSource, AuthStateStorage by storage {

    override fun login(authCode: String) =
        api.requestAccessToken(
            clientId = accessKey,
            clientSecret = clientSecret,
            redirectUri = AuthConfig.redirectUri,
            code = authCode
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(::onAuthSuccess)

    private fun onAuthSuccess(token: AccessToken) {
        state = AuthState.Authenticated(token.access_token)
    }

    override fun loginAnonymous() {
        state = AuthState.Anonymous
    }

    override fun logout() {
        state = AuthState.Unauthenticated
    }
}



