package com.badoo.ribs.routing.source.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.minimal.state.Store
import com.badoo.ribs.minimal.state.TimeCapsule
import com.badoo.ribs.minimal.reactive.map
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistory
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.ACTIVE
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.INACTIVE
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.backstack.operation.NewRoot
import com.badoo.ribs.routing.source.backstack.operation.Remove
import com.badoo.ribs.routing.source.backstack.operation.canPop
import com.badoo.ribs.routing.source.backstack.operation.canPopOverlay
import com.badoo.ribs.routing.source.backstack.operation.pop
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

private val timeCapsuleKey = BackStack::class.java.name
private fun <C : Parcelable> TimeCapsule.initialState(): BackStack.State<C> =
    (get(timeCapsuleKey) ?: BackStack.State())

/**
 * State store implementing [RoutingSource] that keeps a simple linear history of [Routing]s.
 *
 * It will maintain a [RoutingHistory] such the last, and only the last [RoutingHistoryElement] is
 * set to active [RoutingHistoryElement.Activation.ACTIVE].
 *
 * Elements are persisted to Bundle (see [TimeCapsule]) and restored automatically.
 *
 * @see BackStack.Operation for supported operations
 * @see BackStack.accept for logic deciding whether an operation should be carried out
 * @see BackStack.store.apply for the implementation of applying state changes
 * @see BackStack.store.initializeBackstack for operations emitted during initialisation
 */
class BackStack<C : Parcelable> internal constructor(
    private val initialConfiguration: C,
    private val timeCapsule: TimeCapsule
) : RoutingSource<C> {

    @Parcelize
    data class State<C : Parcelable>(
        val id: Int = Random.nextInt(),
        val elements: Elements<C> = emptyList()
    ) : Parcelable, RoutingHistory<C> {

        val current: RoutingHistoryElement<C>?
            get() = elements.lastOrNull()

        override fun iterator(): Iterator<RoutingHistoryElement<C>> =
            elements.iterator()
    }

    interface Operation<C : Parcelable> : (Elements<C>) -> Elements<C> {
        fun isApplicable(elements: Elements<C>): Boolean
    }

    constructor(
        initialConfiguration: C, // TODO consider 2nd constructor with RoutingHistoryElement<C>
        buildParams: BuildParams<*>
    ) : this(
        initialConfiguration,
        TimeCapsule(buildParams.savedInstanceState)
    )

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C> =
        timeCapsule.initialState()

    private val store = object : Store<State<C>>(timeCapsule.initialState()) {
        init {
            timeCapsule.register(timeCapsuleKey) { state }
            initializeBackstack()
        }

        fun accept(operation: Operation<C>) {
            emit(
                state.apply(operation)
            )
        }

        /**
         * Automatically sets [initialConfiguration] as [NewRoot] when initialising the [BackStack]
         */
        private fun initializeBackstack() {
            if (state.elements.isEmpty()) {
                accept(NewRoot(initialConfiguration))
            }
        }

        /**
         * Creates a new [State] based on the old one + the applied [BackStackOperation]
         */
        private fun State<C>.apply(operation: Operation<C>): State<C> {
            val updated = operation
                .invoke(elements)
                .applyBackStackMaintenance()

            return copy(elements = updated)
        }

        // TODO add unit test checking id uniqueness
        private fun Elements<C>.applyBackStackMaintenance(): Elements<C> =
            mapIndexed { index, element ->
                element.copy(
                    activation = if (index == lastIndex) ACTIVE else INACTIVE,
                    routing = routingWithCorrectId(element, index),
                    overlays = overlaysWithCorrectId(element, index)
                )
            }

        private fun routingWithCorrectId(element: RoutingHistoryElement<C>, index: Int): Routing<C> =
            element.routing.copy(
                identifier = state.contentIdForPosition(
                    index,
                    element.routing.configuration
                )
            )

        private fun overlaysWithCorrectId(element: RoutingHistoryElement<C>, index: Int): List<Routing<C>> =
            element.overlays.mapIndexed { overlayIndex, overlay ->
                overlay.copy(
                    identifier = state.overlayIdForPosition(
                        index,
                        element.routing.configuration,
                        overlayIndex,
                        overlay.configuration
                    )
                )
            }
    }

    val activeConfigurations: Source<C> =
        store
            .map {
                it.elements.last()
                    .routing
                    .configuration
            }

    val activeConfiguration: C
        get()= state.elements
            .last()
            .routing
            .configuration

    val state: State<C>
        get() = store.state

    fun popBackStack(): Boolean = // TODO rename
        if (state.elements.canPop) {
            pop()
            true
        } else {
            false
        }

    fun popOverlay(): Boolean =
        if (state.elements.canPopOverlay) {
            pop()
            true
        } else {
            false
        }

    override fun handleBackPressFirst(): Boolean =
        popOverlay()

    override fun handleBackPressFallback(): Boolean =
        popBackStack()

    override fun handleUpNavigation(): Boolean =
        popBackStack()

    override fun remove(identifier: Routing.Identifier) {
        accept(Remove(identifier))
    }

    /**
     * Checks if the required operations are to be executed based on the current [State].
     * Emits corresponding [State]s if the answer is yes.
     */
    fun accept(operation: Operation<C>) {
        if (operation.isApplicable(state.elements)) {
            store.accept(operation)
        }
    }

    override fun observe(callback: (RoutingHistory<C>) -> Unit): Cancellable =
        store.observe(callback)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }
}
