package com.badoo.ribs.routing.state.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.activator.RoutingActivator
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.ACTIVE
import com.badoo.ribs.routing.state.RoutingContext.Resolved
import com.badoo.ribs.routing.state.action.ActionExecutionParams
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Individual.PendingDeactivateFalse
import com.badoo.ribs.routing.state.feature.EffectEmitter
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.transition.TransitionDirection
import com.badoo.ribs.routing.transition.TransitionElement

/**
 * Attaches views of associated [Node]s to a parentNode, and executes the associated [RoutingAction].
 *
 * The [Node]s are expected to be already added to the parentNode on a logical level.
 */
internal class ActivateAction<C : Parcelable>(
    private val emitter: EffectEmitter<C>,
    private val routing: Routing<C>,
    private var item: Resolved<C>,
    private val parentNode: Node<*>,
    private val activator: RoutingActivator<C>,
    private val addedOrRemoved: Boolean,
    private val globalActivationLevel: RoutingContext.ActivationState
) : RoutingTransitionAction<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            params: ActionExecutionParams<C>
        ): RoutingTransitionAction<C> =
            ActivateAction(
                emitter = params.transactionExecutionParams.emitter,
                routing = params.routing,
                item = params.item,
                parentNode = params.transactionExecutionParams.parentNode,
                activator = params.transactionExecutionParams.activator,
                addedOrRemoved = params.addedOrRemoved,
                globalActivationLevel = params.transactionExecutionParams.globalActivationLevel
            )
    }

    override var canExecute: Boolean =
        false

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    override fun onBeforeTransition() {
        val environmentActivated = globalActivationLevel == ACTIVE
        val itemAlreadyActivated = item.activationState == ACTIVE
        canExecute = environmentActivated && !itemAlreadyActivated

        // The least we can do is to mark correct state, this is regardless of executing transitions
        if (!itemAlreadyActivated) {
            emitter.onNext(
                Effect.Individual.Activated(routing, item.copy(activationState = globalActivationLevel))
            )
        }

        if (canExecute) {
            activator.activate(routing, item.nodes)
            prepareTransition()
        }
    }

    private fun prepareTransition() {
        // TODO Consider doing this closer to Router (e.g. result of RoutingActivator.activate)
        transitionElements = item.nodes.mapNotNull {
            it.view?.let { ribView ->
                TransitionElement(
                    configuration = item.routing.configuration, // TODO consider passing the whole RoutingElement
                    direction = TransitionDirection.ENTER,
                    addedOrRemoved = addedOrRemoved,
                    identifier = it.identifier,
                    view = ribView.androidView
                )
            }
        }
    }

    override fun onTransition(forceExecute: Boolean) {
        if (canExecute || forceExecute) {
            item.routingAction.execute()
            activator.onTransitionActivate(routing, item.nodes)
            emitter.onNext(PendingDeactivateFalse(routing))
        }
    }

    override fun onFinish(forceExecute: Boolean) {
    }
}
