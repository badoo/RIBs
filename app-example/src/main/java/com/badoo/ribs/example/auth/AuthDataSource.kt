package com.badoo.ribs.example.auth

import android.util.Log
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

    private fun onAuthSuccess(token: AccessToken) {
        val authState = AuthState.Authenticated(token.accessToken)
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

    override fun getState(): AuthState {
        val authState = stateRelay.value
        return if (authState != null) {
            authState
        } else {
            Log.e("AuthDataSource", "Cannot retrieve auth state")
            AuthState.Unauthenticated
        }
    }


    private fun updateState(authState: AuthState) {
        storage.save(authState)
        stateRelay.accept(authState)
    }
}



