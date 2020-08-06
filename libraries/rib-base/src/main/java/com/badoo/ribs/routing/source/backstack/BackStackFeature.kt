package com.badoo.ribs.routing.source.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.state.Cancellable
import com.badoo.ribs.core.state.Store
import com.badoo.ribs.core.state.TimeCapsule
import com.badoo.ribs.core.state.wrap
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.ACTIVE
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.INACTIVE
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.operation.BackStackOperation
import com.badoo.ribs.routing.source.backstack.operation.NewRoot
import com.badoo.ribs.routing.source.backstack.operation.Remove
import com.badoo.ribs.routing.source.backstack.operation.canPop
import com.badoo.ribs.routing.source.backstack.operation.canPopOverlay
import com.badoo.ribs.routing.source.backstack.operation.pop
import io.reactivex.ObservableSource

private val timeCapsuleKey = BackStackFeature::class.java.name
private fun <C : Parcelable> TimeCapsule.initialState(): BackStackFeatureState<C> =
    (get(timeCapsuleKey) ?: BackStackFeatureState())

/**
 * State store implementing [RoutingSource] that keeps a simple linear history of [Routing]s.
 *
 * It will maintain a [RoutingHistory] such the last, and only the last [RoutingHistoryElement] is
 * set to active [RoutingHistoryElement.Activation.ACTIVE].
 *
 * Elements are persisted to Bundle (see [TimeCapsule]) and restored automatically.
 *
 * @see BackStackFeature.Operation for supported operations
 * @see BackStackFeature.initializeBackstack for operations emitted during initialisation
 * @see BackStackFeature.accept for logic deciding whether an operation should be carried out
 * @see BackStackFeature.apply for the implementation of applying state changes
 */
class BackStackFeature<C : Parcelable> internal constructor(
    private val initialConfiguration: C,
    private val timeCapsule: TimeCapsule
) : RoutingSource<C> {
    /**
     * The back stack operation this [BackStackFeature] supports.
     */
    data class Operation<C : Parcelable>(val backStackOperation: BackStackOperation<C>)

    constructor(
        initialConfiguration: C, // TODO consider 2nd constructor with RoutingHistoryElement<C>
        buildParams: BuildParams<*>
    ) : this(
        initialConfiguration,
        TimeCapsule(buildParams.savedInstanceState)
    )

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C>  =
        timeCapsule.initialState()

    private val store = object : Store<BackStackFeatureState<C>>(timeCapsule.initialState()) {
        init {
            timeCapsule.register(timeCapsuleKey) { state }
            initializeBackstack()
        }

        fun accept(operation: BackStackOperation<C>) {
            emit(
                state.apply(operation)
            )
        }

        /**
         * Automatically sets [initialConfiguration] as [NewRoot] when initialising the [BackStackFeature]
         */
        private fun initializeBackstack() {
            if(state.backStack.isEmpty()) {
                accept(NewRoot(initialConfiguration))
            }
        }

        /**
         * Creates a new [BackStackFeatureState] based on the old one + the applied [BackStackOperation]
         */
        private fun BackStackFeatureState<C>.apply(operation: BackStackOperation<C>): BackStackFeatureState<C> {
            val updated = operation
                .invoke(backStack)
                .applyBackStackMaintenance()

            return copy(backStack = updated)
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
                identifier = contentIdForPosition(
                    index,
                    element.routing.configuration
                )
            )

        private fun overlaysWithCorrectId(element: RoutingHistoryElement<C>, index: Int): List<Routing<C>> =
            element.overlays.mapIndexed { overlayIndex, overlay ->
                overlay.copy(
                    identifier = overlayIdForPosition(
                        index,
                        element.routing.configuration,
                        overlayIndex,
                        overlay.configuration
                    )
                )
            }
    }

    val activeConfiguration: ObservableSource<C> =
        store.wrap()
            .map {
                it.backStack
                    .last()
                    .routing
                    .configuration
            }
            .startWith(initialConfiguration)

    val state: BackStackFeatureState<C>
        get() = store.state

    fun popBackStack(): Boolean = // TODO rename
        if (store.state.backStack.canPop) {
            pop()
            true
        } else {
            false
        }

    fun popOverlay(): Boolean =
        if (store.state.backStack.canPopOverlay) {
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
        accept(
            Operation(
                Remove(identifier)
            )
        )
    }

    /**
     * Checks if the required operations are to be executed based on the current [BackStackFeatureState].
     * Emits corresponding [BackStackFeatureState]s if the answer is yes.
     */
    fun accept(operation: Operation<C>) {
        if (operation.backStackOperation.isApplicable(store.state.backStack)) {
            store.accept(operation.backStackOperation)
        }
    }

    override fun observe(callback: (RoutingHistory<C>) -> Unit): Cancellable =
        store.observe(callback)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }

    internal fun contentIdForPosition(position: Int, content: C): Routing.Identifier =
        Routing.Identifier("Back stack ${System.identityHashCode(this)} #$position = $content")

    internal fun overlayIdForPosition(position: Int, content: C, overlayIndex: Int, overlay: C): Routing.Identifier =
        Routing.Identifier("Back stack ${System.identityHashCode(this)} overlay #$position.$overlayIndex = $content.$overlay")
}
