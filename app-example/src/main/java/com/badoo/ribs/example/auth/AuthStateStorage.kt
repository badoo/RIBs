package com.badoo.ribs.example.auth

import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

interface AuthStateStorage {
    val authUpdates: Observable<AuthState>
    var state: AuthState
}

class AuthStateStorageImpl(
    private val persistence: AuthStatePersistence
) : AuthStateStorage {
    private val stateRelay = BehaviorRelay.createDefault<AuthState>(
        persistence.restore()
    )

    override val authUpdates: Observable<AuthState> = stateRelay
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
