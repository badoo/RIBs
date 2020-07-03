package com.badoo.ribs.example.login.feature

import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.example.auth.AuthDataSource
import com.badoo.ribs.example.login.AuthCodeDataSource
import com.badoo.ribs.example.login.feature.LoginFeature.Effect
import com.badoo.ribs.example.login.feature.LoginFeature.News
import com.badoo.ribs.example.login.feature.LoginFeature.State
import com.badoo.ribs.example.login.feature.LoginFeature.Wish
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.android.schedulers.AndroidSchedulers

internal class LoginFeature(
    authCodeDataSource: AuthCodeDataSource,
    authDataSource: AuthDataSource
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = State(),
    bootstrapper = BootStrapperImpl(authCodeDataSource, authDataSource),
    actor = ActorImpl(),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {

    data class State(
        val yourData: Any? = null
    )

    sealed class Wish {
        object AuthSuccess : Wish()
    }

    sealed class Effect

    sealed class News

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

    class ActorImpl : Actor<State, Wish, Effect> {
        override fun invoke(state: State, wish: Wish): Observable<Effect> =
            empty()
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State =
            state
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? =
            null
    }
}
