package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Effect
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation
import com.badoo.ribs.core.routing.configuration.feature.operation.BackStackOperation
import com.badoo.ribs.core.routing.configuration.feature.operation.newRoot
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
     * The back stack operation this [BackStackFeature] supports.
     */
    data class Operation<C : Parcelable>(val backStackOperation: BackStackOperation<C>)

    /**
     * The set of back stack operations affecting the state.
     */
    sealed class Effect<C : Parcelable> {
        // Consider adding oldState to NewsPublisher
        abstract val oldState: BackStackFeatureState<C>

        data class Applied<C : Parcelable>(
            override val oldState: BackStackFeatureState<C>,
            val backStackOperation: BackStackOperation<C>
        ) : Effect<C>()
    }

    /**
     * Automatically sets [initialConfiguration] as [newRoot] when initialising the [BackStackFeature]
     */
    class BootstrapperImpl<C : Parcelable>(
        private val state: BackStackFeatureState<C>,
        private val initialConfiguration: C
    ) : Bootstrapper<Operation<C>> {
        override fun invoke(): Observable<Operation<C>> = when {
            state.backStack.isEmpty() -> just(newRoot(initialConfiguration))
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
            if (op.backStackOperation.isApplicable(state.backStack)) {
                just(Effect.Applied(state, op.backStackOperation))
            } else {
                empty()
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
            is Effect.Applied -> copy(
                backStack = effect.backStackOperation(backStack)
            )
        }
    }
}
