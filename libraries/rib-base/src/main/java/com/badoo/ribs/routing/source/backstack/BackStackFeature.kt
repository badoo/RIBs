package com.badoo.ribs.routing.source.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.Bootstrapper
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.extension.mapNotNull
import com.badoo.mvicore.feature.ActorReducerFeature
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.ACTIVE
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.INACTIVE
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.BackStackFeature.Operation
import com.badoo.ribs.routing.source.backstack.operation.BackStackOperation
import com.badoo.ribs.routing.source.backstack.operation.NewRoot
import com.badoo.ribs.routing.source.backstack.operation.Remove
import com.badoo.ribs.routing.source.backstack.operation.canPop
import com.badoo.ribs.routing.source.backstack.operation.canPopOverlay
import com.badoo.ribs.routing.source.backstack.operation.pop
import io.reactivex.Observable
import io.reactivex.Observable.empty
import io.reactivex.Observable.just
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer

private val timeCapsuleKey = BackStackFeature::class.java.name
private fun <C : Parcelable> TimeCapsule<BackStackFeatureState<C>>.initialState(): BackStackFeatureState<C> =
    (get(timeCapsuleKey) ?: BackStackFeatureState())

/**
 * State store implementing [RoutingSource] that keeps a simple linear history of [Routing]s.
 *
 * It will maintain a [RoutingHistory] such the last, and only the last [RoutingHistoryElement] is
 * set to active [RoutingHistoryElement.Activation.ACTIVE].
 *
 * Elements are persisted to Bundle (see [AndroidTimeCapsule]) and restored automatically.
 *
 * @see BackStackFeature.Operation for supported operations
 * @see BackStackFeature.BootstrapperImpl for operations emitted during initialisation
 * @see BackStackFeature.ActorImpl for logic deciding whether an operation should be carried out
 * @see BackStackFeature.ReducerImpl for the implementation of applying state changes
 */
class BackStackFeature<C : Parcelable> internal constructor(
    private val initialConfiguration: C,
    private val timeCapsule: AndroidTimeCapsule
)  : Consumer<Operation<C>>, RoutingSource<C> {

    private val feature = ActorReducerFeature<Operation<C>, Effect<C>, BackStackFeatureState<C>, Nothing>(
        initialState = timeCapsule.initialState(),
        bootstrapper = BootstrapperImpl(
            timeCapsule.initialState(),
            initialConfiguration
        ),
        actor = ActorImpl(),
        reducer = ReducerImpl(
            contentIdFactory = this::contentIdForPosition,
            overlayIdFactory = this::overlayIdForPosition
        )
    )

    val state: BackStackFeatureState<C>
        get() = feature.state

    val activeConfiguration: ObservableSource<C>
        get() = Observable.wrap(feature)
            .mapNotNull {
                it.backStack
                    .last()
                    ?.routing
                    ?.configuration
            }
            .startWith(initialConfiguration)

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C>  =
        timeCapsule.initialState()

    constructor(
        initialConfiguration: C, // TODO consider 2nd constructor with RoutingHistoryElement<C>
        buildParams: BuildParams<*>
    ) : this(
        initialConfiguration,
        AndroidTimeCapsule(buildParams.savedInstanceState)
    )

    init {
        timeCapsule.register(timeCapsuleKey) { feature.state }
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
     * Automatically sets [initialConfiguration] as [NewRoot] when initialising the [BackStackFeature]
     */
    class BootstrapperImpl<C : Parcelable>(
        private val state: BackStackFeatureState<C>,
        private val initialConfiguration: C
    ) : Bootstrapper<Operation<C>> {
        override fun invoke(): Observable<Operation<C>> = when {
            state.backStack.isEmpty() -> just(Operation(
                NewRoot(
                    initialConfiguration
                )
            ))
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
                just(
                    Effect.Applied(
                        state,
                        op.backStackOperation
                    )
                )
            } else {
                empty()
            }
    }

    /**
     * Creates a new [State] based on the old one + the applied [Effect]
     */
    @SuppressWarnings("LongMethod")
    class ReducerImpl<C : Parcelable>(
        val contentIdFactory: (Int, C) -> Routing.Identifier,
        val overlayIdFactory: (Int, C, Int, C) -> Routing.Identifier
    ) : Reducer<BackStackFeatureState<C>, Effect<C>> {
        override fun invoke(state: BackStackFeatureState<C>, effect: Effect<C>): BackStackFeatureState<C> =
            state.apply(effect)

        private fun BackStackFeatureState<C>.apply(effect: Effect<C>): BackStackFeatureState<C> = when (effect) {
            is Effect.Applied -> {
                val updated = effect
                    .backStackOperation.invoke(backStack)
                    .applyBackStackMaintenance()

                copy(
                    backStack = updated
                )
            }
        }

        // TODO add unit test checking id uniqueness
        private fun BackStack<C>.applyBackStackMaintenance(): BackStack<C> =
            mapIndexed { index, element ->
                element.copy(
                    activation = if (index == lastIndex) ACTIVE else INACTIVE,
                    routing = routingWithCorrectId(element, index),
                    overlays = overlaysWithCorrectId(element, index)
                )
            }

        private fun routingWithCorrectId(element: RoutingHistoryElement<C>, index: Int): Routing<C> =
            element.routing.copy(
                identifier = contentIdFactory(
                    index,
                    element.routing.configuration
                )
            )

        private fun overlaysWithCorrectId(element: RoutingHistoryElement<C>, index: Int): List<Routing<C>> =
            element.overlays.mapIndexed { overlayIndex, overlay ->
                overlay.copy(
                    identifier = overlayIdFactory(
                        index,
                        element.routing.configuration,
                        overlayIndex,
                        overlay.configuration
                    )
                )
            }
    }

    fun popBackStack(): Boolean = // TODO rename
        if (feature.state.backStack.canPop) {
            pop()
            true
        } else {
            false
        }

    fun popOverlay(): Boolean =
        if (feature.state.backStack.canPopOverlay) {
            pop()
            true
        } else {
            false
        }

    override fun handleBackPressFirst(): Boolean =
        popOverlay()

    override fun handleBackPressFallback(): Boolean =
        popBackStack()

    override fun remove(identifier: Routing.Identifier) {
        feature.accept(Operation(
            Remove(
                identifier
            )
        ))
    }

    override fun accept(operation: Operation<C>) {
        feature.accept(operation)
    }

    override fun subscribe(observer: Observer<in RoutingHistory<C>>) =
        feature.subscribe(observer)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }

    internal fun contentIdForPosition(position: Int, content: C): Routing.Identifier =
        Routing.Identifier("Back stack ${System.identityHashCode(this)} #$position = $content")

    internal fun overlayIdForPosition(position: Int, content: C, overlayIndex: Int, overlay: C): Routing.Identifier =
        Routing.Identifier("Back stack ${System.identityHashCode(this)} overlay #$position.$overlayIndex = $content.$overlay")
}
