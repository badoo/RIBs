package com.badoo.ribs.example.auth

import com.badoo.ribs.example.BuildConfig
import com.badoo.ribs.example.network.UnsplashApi
import com.badoo.ribs.example.network.model.AccessToken
import com.jakewharton.rxrelay2.BehaviorRelay
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
    private val stateRelay = BehaviorRelay.createDefault<AuthState>(storage.restore())

    override val authUpdates: ObservableSource<AuthState> = stateRelay

    override fun login(authCode: String) =
        api.requestAccessToken(
            clientId = accessKey,
            clientSecret = clientSecret,
            redirectUri = AuthConfig.redirectUri,
            code = authCode
        )
            .doOnSuccess(::onAuthSuccess)

    override fun loginAnonymous() {
        val state = AuthState.Anonymous
        updateState(state)
    }

    override fun logout() {
        val state = AuthState.Anonymous
        updateState(state)
    }

    override fun getState(): AuthState =
        if (stateRelay.hasValue()) {
            stateRelay.value!!
        } else throw IllegalStateException("Cannot retrieve auth state")

    private fun onAuthSuccess(token: AccessToken) {
        val authState = AuthState.Authenticated(token.accessToken)
        updateState(authState)
    }

    private fun updateState(authState: AuthState) {
        storage.save(authState)
        stateRelay.accept(authState)
    }
}



