package com.badoo.ribs.routing.source.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.state.Store
import com.badoo.ribs.core.state.wrap
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
 * @see BackStackFeature.initializeBackstack for operations emitted during initialisation
 * @see BackStackFeature.accept for logic deciding whether an operation should be carried out
 * @see BackStackFeature.reduceEvent for the implementation of applying state changes
 */
class BackStackFeature<C : Parcelable> internal constructor(
    private val initialConfiguration: C,
    private val timeCapsule: AndroidTimeCapsule
) : Store<BackStackFeatureState<C>>(timeCapsule.initialState()),
    Consumer<Operation<C>>,
    RoutingSource<C> {

    /**
     * The back stack operation this [BackStackFeature] supports.
     */
    data class Operation<C : Parcelable>(val backStackOperation: BackStackOperation<C>)

    val activeConfiguration: ObservableSource<C> =
        wrap()
            .map {
                it.backStack
                    .last()
                    .routing
                    .configuration
            }
            .startWith(initialConfiguration)

    constructor(
        initialConfiguration: C, // TODO consider 2nd constructor with RoutingHistoryElement<C>
        buildParams: BuildParams<*>
    ) : this(
        initialConfiguration,
        AndroidTimeCapsule(buildParams.savedInstanceState)
    )

    init {
        timeCapsule.register(timeCapsuleKey) { state }
        initializeBackstack()
    }

    override fun baseLineState(fromRestored: Boolean): RoutingHistory<C>  =
        timeCapsule.initialState()

    /**
     * Automatically sets [initialConfiguration] as [NewRoot] when initialising the [BackStackFeature]
     */
    private fun initializeBackstack() {
        if(state.backStack.isEmpty()) {
            emit(
                state.apply(
                    NewRoot(
                        initialConfiguration
                    )
                )
            )
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

    fun popBackStack(): Boolean = // TODO rename
        if (state.backStack.canPop) {
            pop()
            true
        } else {
            false
        }

    fun popOverlay(): Boolean =
        if (state.backStack.canPopOverlay) {
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
     * Checks if the required operations are to be executed based on the current [State].
     * Emits corresponding [Effect]s if the answer is yes.
     */
    override fun accept(operation: Operation<C>) {
        if (operation.backStackOperation.isApplicable(state.backStack)) {
            emit(
                state.apply(operation.backStackOperation)
            )
        }
    }

    override fun subscribe(observer: Observer<in RoutingHistory<C>>) =
        wrap().subscribe(observer)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        timeCapsule.saveState(outState)
    }

    internal fun contentIdForPosition(position: Int, content: C): Routing.Identifier =
        Routing.Identifier("Back stack ${System.identityHashCode(this)} #$position = $content")

    internal fun overlayIdForPosition(position: Int, content: C, overlayIndex: Int, overlay: C): Routing.Identifier =
        Routing.Identifier("Back stack ${System.identityHashCode(this)} overlay #$position.$overlayIndex = $content.$overlay")
}
