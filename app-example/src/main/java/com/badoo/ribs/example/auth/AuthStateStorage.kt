package com.badoo.ribs.example.auth

import android.content.SharedPreferences
import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers

interface AuthStateStorage {
    val authUpdates: ObservableSource<AuthState>
    fun getState(): AuthState
    fun save(state: AuthState)
    fun restore(): AuthState
}

class PreferencesAuthStateStorage(
    private val preferences: SharedPreferences
) : AuthStateStorage {
    private val stateRelay = BehaviorRelay.createDefault<AuthState>(restore())

    override val authUpdates: ObservableSource<AuthState> =
        stateRelay.observeOn(AndroidSchedulers.mainThread())

    override fun getState(): AuthState {
        val authState = stateRelay.value
        return if (authState != null) {
            authState
        } else {
            Log.e("AuthDataSource", "Cannot retrieve auth state")
            AuthState.Unauthenticated
        }
    }

    override fun save(state: AuthState) {
        when (state) {
            is AuthState.Unauthenticated -> preferences.edit()
                .clear()
                .apply()
            is AuthState.Anonymous -> preferences.edit()
                .clear()
                .putBoolean(ANONYMOUS_AUTH_KEY, true)
                .apply()
            is AuthState.Authenticated -> preferences.edit()
                .clear()
                .putString(ACCESS_TOKEN_KEY, state.accessToken)
                .apply()
        }
        stateRelay.accept(state)
    }

    override fun restore(): AuthState {
        val accessToken = preferences.getString(ACCESS_TOKEN_KEY, null)
        val isAnonymousAuth = preferences.getBoolean(ANONYMOUS_AUTH_KEY, false)
        return when {
            accessToken != null -> AuthState.Authenticated(accessToken)
            isAnonymousAuth -> AuthState.Anonymous
            else -> AuthState.Unauthenticated
        }
    }

    private companion object {
        const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
        const val ANONYMOUS_AUTH_KEY = "ANONYMOUS_AUTH"
    }
}
