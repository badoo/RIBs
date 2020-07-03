package com.badoo.ribs.example.auth

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers

interface AuthStateStorage {
    val authUpdates: ObservableSource<AuthState>
    var state: AuthState
}

class AuthStateStorageImpl(
    private val persistence: AuthStatePersistence
) : AuthStateStorage {
    private val stateRelay = BehaviorRelay.createDefault<AuthState>(
        persistence.restore()
    )

    override val authUpdates: ObservableSource<AuthState> =
        stateRelay.observeOn(AndroidSchedulers.mainThread())
    override var state: AuthState
        get() {
            val authState = stateRelay.value
            return if (authState != null) {
                authState
            } else {
                Log.e("AuthDataSource", "Cannot retrieve auth state")
                AuthState.Unauthenticated
            }
        }
        set(value) {
            persistence.save(value)
            stateRelay.accept(value)
        }

}
