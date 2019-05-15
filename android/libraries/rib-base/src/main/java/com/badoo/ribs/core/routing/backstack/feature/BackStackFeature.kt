package com.badoo.ribs.core.routing.backstack.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.BaseFeature
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Action
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Action.Execute
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Effect
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.State
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.NewRoot
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Pop
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Push
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.PushOverlay
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Wish.Replace
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import kotlinx.android.parcel.Parcelize

internal class BackStackFeature<C : Parcelable>(
    initialConfiguration: C,
    timeCapsule: TimeCapsule<State<C>>,
    tag: String = "BackStackFeature.State"
): BaseFeature<Wish<C>, Action<C>, Effect<C>, State<C>, Nothing>(
    initialState = timeCapsule[tag] ?: State(),
    wishToAction = { Execute(it) },
    bootstrapper = BooststrapperImpl(
        timeCapsule[tag] ?: State(),
        initialConfiguration
    ),
    actor = ActorImpl<C>(),
    reducer = ReducerImpl<C>()
) {
    init {
        timeCapsule.register(tag) { state }
    }

    @Parcelize
    data class State<C : Parcelable>(
        val backStack: List<C> = emptyList()
    ) : Parcelable {
        val current: C
            get() = backStack.last()

        val canPop: Boolean
            get() = backStack.size > 1
    }

    class BooststrapperImpl<C : Parcelable>(
        private val state: State<C>,
        private val initialConfiguration: C
    ) : Bootstrapper<Action<C>> {
        override fun invoke(): Observable<Action<C>> = when {
            state.backStack.isEmpty() -> just(Execute(NewRoot(initialConfiguration)))
            else -> empty()
        }
    }

    sealed class Wish<C : Parcelable> {
        data class Replace<C : Parcelable>(val configuration: C) : Wish<C>()
        data class Push<C : Parcelable>(val configuration: C) : Wish<C>()
        data class PushOverlay<C : Parcelable>(val configuration: C) : Wish<C>()
        data class NewRoot<C : Parcelable>(val configuration: C) : Wish<C>()
        class Pop<C : Parcelable> : Wish<C>()
    }

    sealed class Action<C : Parcelable> {
        data class Execute<C : Parcelable>(val wish: Wish<C>) : Action<C>()
    }

    sealed class Effect<C : Parcelable> {
        // Consider adding oldState to NewsPublisher
        abstract val oldState: State<C>

        data class Replace<C : Parcelable>(
            override val oldState: State<C>,
            val configuration: C
        ) : Effect<C>()

        data class Push<C : Parcelable>(
            override val oldState: State<C>,
            val configuration: C
        ) : Effect<C>()

        data class PushOverlay<C : Parcelable>(
            override val oldState: State<C>,
            val configuration: C
        ) : Effect<C>()

        data class NewRoot<C : Parcelable>(
            override val oldState: State<C>,
            val configuration: C
        ) : Effect<C>()

        data class Pop<C : Parcelable>(
            override val oldState: State<C>
        ) : Effect<C>()
    }

    class ActorImpl<C : Parcelable> : Actor<State<C>, Action<C>, Effect<C>> {
        override fun invoke(state: State<C>, action: Action<C>): Observable<out Effect<C>> =
            when (action) {
                is Execute -> {
                    when (val wish = action.wish) {
                        is Replace -> when {
                            wish.configuration != state.current -> {
                                just(Effect.Replace(state, wish.configuration))
                            }
                            else -> empty()
                        }

                        is Push -> when {
                            wish.configuration != state.current ->
                                just(Effect.Push(state, wish.configuration))
                            else -> empty()
                        }

                        is PushOverlay -> when {
                            wish.configuration != state.current ->
                                just(Effect.PushOverlay(state, wish.configuration))
                            else -> empty()
                        }

                        is NewRoot -> when {
                            state.backStack != listOf(wish.configuration) ->
                                just(Effect.NewRoot(state, wish.configuration))
                            else -> empty()
                        }


                        is Pop -> when {
                            state.canPop -> just(Effect.Pop(state))
                            else -> empty()
                        }
                    }
                }
            }
    }

    class ReducerImpl<C : Parcelable> : Reducer<State<C>, Effect<C>> {
        @SuppressWarnings("LongMethod")
        override fun invoke(state: State<C>, effect: Effect<C>): State<C> = when (effect) {
            is Effect.Replace -> state.copy(
                backStack = state.backStack.dropLast(1) + effect.configuration
            )
            is Effect.Push -> state.copy(
                backStack = state.backStack + effect.configuration
            )
            is Effect.PushOverlay -> state.copy(
                backStack = state.backStack + effect.configuration
            )
            is Effect.NewRoot -> state.copy(
                backStack = listOf(effect.configuration)
            )
            is Effect.Pop -> state.copy(
                backStack = state.backStack.dropLast(1)
            )
        }
    }
}
