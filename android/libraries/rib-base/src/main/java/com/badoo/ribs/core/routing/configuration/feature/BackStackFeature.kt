package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Effect
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation.NewRoot
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation.Pop
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation.Push
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation.PushOverlay
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation.Replace
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation.SingleTop
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just

private val timeCapsuleKey = BackStackFeature::class.java.name
private fun <C : Parcelable> TimeCapsule<BackStackFeatureState<C>>.initialState(): BackStackFeatureState<C> =
    (get(timeCapsuleKey) ?: BackStackFeatureState())

/**
 * State store responsible for the changes of the logical back stack (described as a list of [C]
 * elements in [BackStackFeature.State]).
 *
 * Does nothing beyond the manipulation of the list of [C] elements.
 *
 * @see BackStackFeature.Operation for supported operations
 * @see BackStackFeature.BootstrapperImpl for operations emitted during initialisation
 * @see BackStackFeature.ActorImpl for logic deciding whether an operation should be carried out
 * @see BackStackFeature.ReducerImpl for the implementation of applying state changes
 */
internal class BackStackFeature<C : Parcelable>(
    initialConfiguration: C,
    timeCapsule: TimeCapsule<BackStackFeatureState<C>>
): ActorReducerFeature<Operation<C>, Effect<C>, BackStackFeatureState<C>, Nothing>(
    initialState = timeCapsule.initialState(),
    bootstrapper = BootstrapperImpl(
        timeCapsule.initialState(),
        initialConfiguration
    ),
    actor = ActorImpl<C>(),
    reducer = ReducerImpl<C>()
) {
    val initialState =
        timeCapsule.initialState()

    init {
        timeCapsule.register(timeCapsuleKey) { state }
    }

    /**
     * The set of back stack operations this [BackStackFeature] supports.
     */
    sealed class Operation<C : Parcelable> {
        data class Replace<C : Parcelable>(val configuration: C) : Operation<C>()
        data class Push<C : Parcelable>(val configuration: C) : Operation<C>()
        data class PushOverlay<C : Parcelable>(val configuration: C) : Operation<C>()
        data class SingleTop<C : Parcelable>(val configuration: C) : Operation<C>()
        data class NewRoot<C : Parcelable>(val configuration: C) : Operation<C>()
        class Pop<C : Parcelable> : Operation<C>()
    }

    /**
     * The set of back stack operations affecting the state.
     */
    sealed class Effect<C : Parcelable> {
        // Consider adding oldState to NewsPublisher
        abstract val oldState: BackStackFeatureState<C>

        data class NewRoot<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>,
            val configuration: C
        ) : Effect<C>()

        data class Replace<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>,
            val configuration: C
        ) : Effect<C>()

        data class Push<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>,
            val configuration: C
        ) : Effect<C>()

        data class PushOverlay<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>,
            val configuration: C
        ) : Effect<C>()

        data class SingleTopReactivate<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>,
            val position: Int
        ) : Effect<C>()

        data class SingleTopReplace<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>,
            val position: Int,
            val configuration: C
        ) : Effect<C>()

        data class PopOverlay<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>
        ) : Effect<C>()

        data class PopContent<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>
        ) : Effect<C>()
    }

    /**
     * Automatically sets [initialConfiguration] as [NewRoot] when initialising the [BackStackFeature]
     */
    class BootstrapperImpl<C : Parcelable>(
        private val state: BackStackFeatureState<C>,
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
    class ActorImpl<C : Parcelable> : Actor<BackStackFeatureState<C>, Operation<C>, Effect<C>> {
        @SuppressWarnings("LongMethod")
        override fun invoke(state: BackStackFeatureState<C>, op: Operation<C>): Observable<out Effect<C>> =
            when (op) {
                is Replace -> when {
                    op.configuration != state.current?.configuration -> {
                        just(Effect.Replace(state, op.configuration))
                    }
                    else -> empty()
                }

                is Push -> when {
                    op.configuration != state.current?.configuration ->
                        just(Effect.Push(state, op.configuration))
                    else -> empty()
                }

                is PushOverlay -> when {
                    state.backStack.isNotEmpty() && op.configuration != state.currentOverlay ->
                        just(Effect.PushOverlay(state, op.configuration))
                    else -> empty()
                }

                is SingleTop -> {
                    val targetClass = op.configuration.javaClass
                    val lastIndexOfSameClass = state.backStack.indexOfLast {
                        targetClass.isInstance(it.configuration)
                    }

                    if (lastIndexOfSameClass == -1) {
                        just(Effect.Push(state, op.configuration))
                    } else {
                        if (state.backStack[lastIndexOfSameClass] == op.configuration) {
                            just(Effect.SingleTopReactivate(
                                oldState = state,
                                position = lastIndexOfSameClass
                            ))
                        } else {
                            just(Effect.SingleTopReplace(
                                oldState = state,
                                position = lastIndexOfSameClass,
                                configuration = op.configuration
                            ))
                        }
                    }
                }

                is NewRoot -> when {
                    state.backStack.size != 1 || state.backStack.first().configuration != op.configuration ->
                        just(Effect.NewRoot(state, op.configuration))
                    else -> empty()
                }

                is Pop -> when {
                    state.canPopOverlay -> just(Effect.PopOverlay(state))
                    state.canPopContent -> just(Effect.PopContent(state))
                    else -> empty()
                }
            }
    }

    /**
     * Creates a new [State] based on the old one + the applied [Effect]
     */
    @SuppressWarnings("LongMethod")
    class ReducerImpl<C : Parcelable> : Reducer<BackStackFeatureState<C>, Effect<C>> {
        override fun invoke(state: BackStackFeatureState<C>, effect: Effect<C>): BackStackFeatureState<C> =
            state.apply(effect)

        private fun BackStackFeatureState<C>.apply(effect: Effect<C>): BackStackFeatureState<C>  = when (effect) {
            is Effect.NewRoot -> copy(
                backStack = listOf(BackStackElement(effect.configuration))
            )
            is Effect.Replace -> copy(
                backStack = backStack.dropLast(1) + BackStackElement(effect.configuration)
            )
            is Effect.Push -> copy(
                backStack = backStack + BackStackElement(effect.configuration)
            )
            is Effect.PushOverlay -> copy(
                backStack = backStack.replaceLastWith(
                    backStack.last().copy(
                        overlays = backStack.last().overlays + effect.configuration
                    )
                )
            )
            is Effect.SingleTopReactivate -> copy (
                backStack = backStack.dropLast(backStack.size - effect.position - 1)
            )
            is Effect.SingleTopReplace -> copy (
                backStack = backStack.dropLast(backStack.size - effect.position) + BackStackElement(effect.configuration)
            )
            is Effect.PopOverlay -> copy(
                backStack = backStack.replaceLastWith(
                    backStack.last().copy(
                        overlays = backStack.last().overlays.dropLast(1)
                    )
                )
            )
            is Effect.PopContent -> copy(
                backStack = backStack.dropLast(1)
            )
        }

        private fun List<BackStackElement<C>>.replaceLastWith(replacement: BackStackElement<C>): List<BackStackElement<C>> =
            toMutableList().apply { set(lastIndex, replacement) }
    }
}
