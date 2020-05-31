package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.activator.RoutingActivator
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect.Individual.Deactivated
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect.Individual.PendingDeactivateTrue
import com.badoo.ribs.core.routing.configuration.feature.EffectEmitter
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Detaches views of associated [Node]s to a parentNode, and cleans up the associated [RoutingAction].
 *
 * Will not detach the [Node]s on the logical level, they are kept alive without their views.
 */
internal class DeactivateAction<C : Parcelable>(
    private val emitter: EffectEmitter<C>,
    private val routing: Routing<C>,
    private var item: Resolved<C>,
    private val parentNode: Node<*>,
    private val activator: RoutingActivator<C>,
    private val isBackStackOperation: Boolean,
    private val targetActivationState: ActivationState = INACTIVE
) : Action<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            params: ActionExecutionParams<C>
        ): Action<C> = DeactivateAction(
            emitter = params.transactionExecutionParams.emitter,
            routing = params.routing,
            item = params.item,
            parentNode = params.transactionExecutionParams.parentNode,
            activator = params.transactionExecutionParams.activator,
            isBackStackOperation = params.isBackStackOperation
        )
    }

    override var canExecute: Boolean =
        true

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    override fun onBeforeTransition() {
        // TODO Consider doing this closer to Router (in result of RoutingActivator)
        transitionElements = item.nodes.mapNotNull {
            it.view?.let { ribView ->
                TransitionElement(
                    configuration = item.routing.configuration, // TODO consider passing the whole RoutingElement
                    direction = TransitionDirection.EXIT,
                    isBackStackOperation = isBackStackOperation,
                    parentViewGroup = parentNode.targetViewGroupForChild(it),
                    identifier = it.identifier,
                    view = ribView.androidView
                )
            }
        }
    }

    override fun onTransition(forceExecute: Boolean) {
        if (canExecute || forceExecute) {
            item.routingAction.cleanup()
            activator.onTransitionDeactivate(routing, item.nodes)
            emitter.onNext(PendingDeactivateTrue(routing))
        }
    }

    override fun onFinish(forceExecute: Boolean) {
        if (canExecute || forceExecute) {
            activator.deactivate(routing, item.nodes)
            emitter.onNext(Deactivated(routing, item.copy(activationState = targetActivationState)))
        }
    }
}
