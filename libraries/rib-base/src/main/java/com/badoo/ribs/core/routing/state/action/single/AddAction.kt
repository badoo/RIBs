package com.badoo.ribs.core.routing.state.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.activator.RoutingActivator
import com.badoo.ribs.core.routing.state.RoutingContext.Resolved
import com.badoo.ribs.core.routing.state.action.ActionExecutionParams
import com.badoo.ribs.core.routing.state.feature.ConfigurationFeature.Effect.Individual.Added
import com.badoo.ribs.core.routing.state.feature.ConfigurationFeature.Effect.Individual.PendingRemovalFalse
import com.badoo.ribs.core.routing.state.feature.EffectEmitter
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.transition.TransitionElement

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
        emitter.onNext(Added(routing, item))
    }

    override fun onTransition(forceExecute: Boolean) {
        activator.onTransitionAdd(routing, item.nodes)
        emitter.onNext(PendingRemovalFalse(routing))
    }

    override fun onFinish(forceExecute: Boolean) {
    }

    override val transitionElements: List<TransitionElement<C>> =
        emptyList()
}
