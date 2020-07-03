package com.badoo.ribs.example.auth

import com.badoo.ribs.example.BuildConfig
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.network.model.AccessToken
import io.reactivex.ObservableSource
import io.reactivex.Single

interface AuthDataSource {
    val authUpdates: ObservableSource<AuthState>
    fun login(authCode: String): Single<AccessToken>
    fun loginAnonymous()
    fun logout()
    fun getState(): AuthState
}

class AuthDataSourceImpl(
    private val api: UnsplashApi,
    private val storage: AuthStateStorage,
    private val accessKey: String = BuildConfig.ACCESS_KEY,
    private val clientSecret: String = BuildConfig.CLIENT_SECRET
) : AuthDataSource {
    override val authUpdates: ObservableSource<AuthState> = storage.authUpdates

    override fun login(authCode: String) =
        api.requestAccessToken(
            clientId = accessKey,
            clientSecret = clientSecret,
            redirectUri = AuthConfig.redirectUri,
            code = authCode
        )
            .doOnSuccess(::onAuthSuccess)

    private fun onAuthSuccess(token: AccessToken) {
        val authState = AuthState.Authenticated(token.access_token)
        updateState(authState)
    }

    override fun loginAnonymous() {
        val state = AuthState.Anonymous
        updateState(state)
    }

    override fun logout() {
        val state = AuthState.Unauthenticated
        updateState(state)
    }

    override fun getState(): AuthState = storage.getState()

    private fun updateState(authState: AuthState) {
        storage.save(authState)
    }
}



