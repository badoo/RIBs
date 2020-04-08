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
) : ActorReducerFeature<Operation<C>, Effect<C>, BackStackFeatureState<C>, Nothing>(
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
        data class ExtendedOperation<C : Parcelable>(val backStackOperation: BackStackOperation<C>) : Operation<C>()
        data class SingleTop<C : Parcelable>(val configuration: C) : Operation<C>()
    }

    /**
     * The set of back stack operations affecting the state.
     */
    sealed class Effect<C : Parcelable> {
        // Consider adding oldState to NewsPublisher
        abstract val oldState: BackStackFeatureState<C>

        data class ExtendOperationApplied<C : Parcelable>(
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
            val backStackOperation: BackStackOperation<C>
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

                is ExtendedOperation -> when {
                    op.backStackOperation.isApplicable(state.backStack) -> just(Effect.ExtendOperationApplied(state, op.backStackOperation))
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

        private fun BackStackFeatureState<C>.apply(effect: Effect<C>): BackStackFeatureState<C> = when (effect) {
            is Effect.SingleTopReactivate -> copy (
                backStack = backStack.dropLast(backStack.size - effect.position - 1)
            )
            is Effect.SingleTopReplace -> copy (
                backStack = backStack.dropLast(backStack.size - effect.position) + BackStackElement(effect.configuration)
            )
            is Effect.ExtendOperationApplied -> copy(
                backStack = effect.backStackOperation.modifyStack(backStack)
            )
        }
    }
}
