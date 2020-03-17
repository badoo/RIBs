package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Attaches views of associated [Node]s to a parentNode, and executes the associated [RoutingAction].
 *
 * The [Node]s are expected to be already added to the parentNode on a logical level.
 */
internal class ActivateAction<C : Parcelable>(
    private var item: Resolved<C>,
    private val params: ActionExecutionParams<C>,
    private val isBackStackOperation: Boolean
) : ReversibleAction<C>() {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            key: ConfigurationKey,
            params: ActionExecutionParams<C>,
            isBackStackOperation: Boolean
        ): Action<C> {
            val item = params.resolver.invoke(key)
            return ActivateAction(item.first, params, isBackStackOperation)
        }
    }

    private var canExecute: Boolean =
        false

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    private var actionableNodes: List<Node.Descriptor> =
        emptyList()


    override var result: Resolved<C> =
        // TODO check if can be merged with [CappedLifecycle], as this one has the same conceptual effect
        item.copy(activationState = params.globalActivationLevel)

    override fun onBeforeTransition() {
        canExecute = when {
            // If there's no view available (i.e. globalActivationLevel == SLEEPING) we must not execute
            // routing actions or try to attach view. That will be done on next WakeUp. For now, let's
            // just mark the element to the same value.
            params.globalActivationLevel != ACTIVE -> false
            // Don't execute activation twice
            item.activationState == ACTIVE -> false
            else -> true
        }

        if (canExecute) {
            prepareTransition()
        }
    }

    private fun prepareTransition() {
        actionableNodes = item.nodes
            .filter { it.viewAttachMode == Node.AttachMode.PARENT && !it.node.isAttachedToView }

        actionableNodes.forEach {
            params.parentNode.createChildView(it.node)
            params.parentNode.attachChildView(it.node)
        }

        transitionElements = actionableNodes.mapNotNull {
            it.node.view?.let { ribView ->
                TransitionElement(
                    configuration = item.configuration,
                    direction = TransitionDirection.ENTER,
                    isBackStackOperation = isBackStackOperation,
                    parentViewGroup = params.parentNode.targetViewGroupForChild(it.node),
                    identifier = it.node.identifier,
                    view = ribView.androidView
                )
            }
        }
    }

    override fun onTransition() {
        if (canExecute) {
            if (isReversed) {
                item.routingAction.cleanup()
            } else {
                item.routingAction.execute()
            }
        }
    }

    override fun onFinish() {
        if (isReversed) {
            actionableNodes.forEach {
                params.parentNode.detachChildView(it.node)
            }
        }
    }
}
