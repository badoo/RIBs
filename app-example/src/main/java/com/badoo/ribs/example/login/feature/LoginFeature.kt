package com.badoo.ribs.example.login.feature

import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ReducerFeature
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.login.AuthCodeDataSource
import com.badoo.ribs.example.login.feature.LoginFeature.State
import com.badoo.ribs.example.login.feature.LoginFeature.Wish
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

internal class LoginFeature(
    authCodeDataSource: AuthCodeDataSource,
    authDataSource: AuthDataSource
) : ReducerFeature<Wish, State, Nothing>(
    initialState = State(),
    bootstrapper = BootStrapperImpl(authCodeDataSource, authDataSource),
    reducer = ReducerImpl()
) {

    data class State(
        val yourData: Any? = null
    )

    sealed class Wish {
        object AuthSuccess : Wish()
    }

    class BootStrapperImpl(
        private val authCodeDataSource: AuthCodeDataSource,
        private val authDataSource: AuthDataSource
    ) : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> =
            authCodeDataSource.getAuthCodeUpdates()
                .flatMap { authCode ->
                    authDataSource.login(authCode).toObservable()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    Wish.AuthSuccess
                }
    }

    class ReducerImpl : Reducer<State, Wish> {
        override fun invoke(state: State, wish: Wish): State =
            state
    }
}
