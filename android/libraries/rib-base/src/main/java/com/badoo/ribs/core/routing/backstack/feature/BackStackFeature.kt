package com.badoo.ribs.core.routing.backstack.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Effect
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.State
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.NewRoot
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.Pop
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.Push
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.PushOverlay
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.Operation.Replace
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import kotlinx.android.parcel.Parcelize

private val timeCapsuleKey = BackStackFeature::class.java.name
private fun <C : Parcelable> TimeCapsule<State<C>>.initialState(): State<C> =
    (get(timeCapsuleKey) ?: State())

/**
 * State store responsible for the changes of the logical back stack (described as a list of [C]
 * elements in [BackStackFeature.State]).
 *
 * Does nothing beyond the manipulation of the list of [C] elements.
 *
 * @see BackStackFeature.Operation for supported operations
 * @see BackStackFeature.BooststrapperImpl for operations emitted during initialisation
 * @see BackStackFeature.ActorImpl for logic deciding whether an operation should be carried out
 * @see BackStackFeature.ReducerImpl for the implementation of applying state changes
 */
internal class BackStackFeature<C : Parcelable>(
    initialConfiguration: C,
    timeCapsule: TimeCapsule<State<C>>
): ActorReducerFeature<Operation<C>, Effect<C>, State<C>, Nothing>(
    initialState = timeCapsule.initialState(),
    bootstrapper = BooststrapperImpl(
        timeCapsule.initialState(),
        initialConfiguration
    ),
    actor = ActorImpl<C>(),
    reducer = ReducerImpl<C>()
) {
    init {
        timeCapsule.register(timeCapsuleKey) { state }
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

    /**
     * The set of back stack operations this [BackStackFeature] supports.
     */
    sealed class Operation<C : Parcelable> {
        data class Replace<C : Parcelable>(val configuration: C) : Operation<C>()
        data class Push<C : Parcelable>(val configuration: C) : Operation<C>()
        data class PushOverlay<C : Parcelable>(val configuration: C) : Operation<C>()
        data class NewRoot<C : Parcelable>(val configuration: C) : Operation<C>()
        class Pop<C : Parcelable> : Operation<C>()
    }

    /**
     * The set of back stack operations affecting the state.
     */
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

    /**
     * Automatically sets [initialConfiguration] as [NewRoot] when initialising the [BackStackFeature]
     */
    class BooststrapperImpl<C : Parcelable>(
        private val state: State<C>,
        private val initialConfiguration: C
    ) : Bootstrapper<Operation<C>> {
        override fun invoke(): Observable<Operation<C>> = when {
            state.backStack.isEmpty() -> just(NewRoot(initialConfiguration))
            else -> empty()
        }
    }

    /**
     * Checks if the required operations are to be executed based on the current [State].
     * Emits corresponding [Effect]s if the answer is yes.
     */
    class ActorImpl<C : Parcelable> : Actor<State<C>, Operation<C>, Effect<C>> {
        @SuppressWarnings("LongMethod")
        override fun invoke(state: State<C>, op: Operation<C>): Observable<out Effect<C>> =
            when (op) {
                is Replace -> when {
                    op.configuration != state.current -> {
                        just(Effect.Replace(state, op.configuration))
                    }
                    else -> empty()
                }

                is Push -> when {
                    op.configuration != state.current ->
                        just(Effect.Push(state, op.configuration))
                    else -> empty()
                }

                is PushOverlay -> when {
                    op.configuration != state.current ->
                        just(Effect.PushOverlay(state, op.configuration))
                    else -> empty()
                }

                is NewRoot -> when {
                    state.backStack != listOf(op.configuration) ->
                        just(Effect.NewRoot(state, op.configuration))
                    else -> empty()
                }

                is Pop -> when {
                    state.canPop -> just(Effect.Pop(state))
                    else -> empty()
                }
            }
    }

    /**
     * Creates a new [State] based on the old one + the applied [Effect]
     */
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
