package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionCallbacks
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.configuration.feature.EffectEmitter
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

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
    private val callbacks: ActionExecutionCallbacks<C>,
    private val actionableNodes: List<Node<*>>,
    private val isBackStackOperation: Boolean,
    private val globalActivationLevel: ConfigurationContext.ActivationState
) : Action<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            params: ActionExecutionParams<C>,
            actionableNodes: List<Node<*>>
        ): Action<C> =
            ActivateAction(
                emitter = params.transactionExecutionParams.emitter,
                routing = params.routing,
                item = params.item,
                parentNode = params.transactionExecutionParams.parentNode,
                callbacks = params.callbacks,
                actionableNodes = actionableNodes,
                isBackStackOperation = params.isBackStackOperation,
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

            // TODO make the difference between actionableNodes (e.g. AttachMode.PARENT) vs item.nodes (all nodes)
            //  more pronounced. Here we want to notify callback of any node (especially those with AttachMode.EXTERNAL)
            //  so that they can be attached by external handler.
            //  Consider
            callbacks.onActivated(routing, item.nodes)
        }

        if (canExecute) {
            prepareTransition()
        }
    }

    /**
     * TODO Consider doing this in Router when callback is fired. This would fix
     *  actionableNodes vs item.nodes disparity here and make the decision more pronounced.
     */
    private fun prepareTransition() {
        actionableNodes.forEach {
            parentNode.createChildView(it)
            parentNode.attachChildView(it)
        }

        transitionElements = actionableNodes.mapNotNull {
            it.view?.let { ribView ->
                TransitionElement(
                    configuration = item.routing.configuration, // TODO consider passing the whole RoutingElement
                    direction = TransitionDirection.ENTER,
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
            item.routingAction.execute()

            emitter.onNext(
                Effect.Individual.PendingDeactivateFalse(routing)
            )
        }
    }

    override fun onFinish(forceExecute: Boolean) {
    }
}
