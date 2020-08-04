package com.badoo.ribs.routing.state.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.activator.RoutingActivator
import com.badoo.ribs.routing.state.RoutingContext.Resolved
import com.badoo.ribs.routing.state.action.ActionExecutionParams
import com.badoo.ribs.routing.state.feature.EffectEmitter
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Individual.Added
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Individual.PendingRemovalFalse
import com.badoo.ribs.routing.transition.TransitionElement

/**
 * Attaches [Node]s to a parentNode without their views
 */
internal class AddAction<C : Parcelable>(
    private val emitter: EffectEmitter<C>,
    private val routing: Routing<C>,
    private var item: Resolved<C>,
    private val activator: RoutingActivator<C>
) : RoutingTransitionAction<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            params: ActionExecutionParams<C>
        ): RoutingTransitionAction<C> = AddAction(
            emitter = params.transactionExecutionParams.emitter,
            routing = params.routing,
            item = params.item,
            activator = params.transactionExecutionParams.activator
        )
    }

    override var canExecute: Boolean =
        true

    override fun onBeforeTransition() {
        activator.add(routing, item.nodes)
        emitter.invoke(Added(routing, item))
    }

    override fun onTransition(forceExecute: Boolean) {
        activator.onTransitionAdd(routing, item.nodes)
        emitter.invoke(PendingRemovalFalse(routing))
    }

    override fun onFinish(forceExecute: Boolean) {
    }

    override val transitionElements: List<TransitionElement<C>> =
        emptyList()
}
