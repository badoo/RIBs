package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.WorkingState
import com.badoo.ribs.core.view.TransitionDirection
import com.badoo.ribs.core.view.TransitionElement

/**
 * Attaches views of associated [Node]s to a parentNode, and executes the associated [RoutingAction].
 *
 * The [Node]s are expected to be already added to the parentNode on a logical level.
 */
internal object ActivateAction : ResolvedSingleConfigurationAction() {

    /**
     * @return the item with its [ConfigurationContext.activationState] set to the global [WorkingState.activationLevel]
     *         value. This is important because if the global state is [SLEEPING], the individual
     *         element cannot go directly to [ACTIVE], as that should only be done on the next [WakeUp] command
     */
    override fun <C : Parcelable> execute(item: Resolved<C>, params: ActionExecutionParams<C>): Resolved<C> {
        val (_, parentNode, globalActivationLevel) = params

        // If there's no view available (i.e. globalActivationLevel == SLEEPING) we must not execute
        // routing actions or try to attach view. That will be done on next WakeUp. For now, let's
        // just mark the element to the same value.
        if (globalActivationLevel != ACTIVE) {
            return item.withActivationState(globalActivationLevel)
        }

        // Don't execute activation twice
        if (item.activationState == ACTIVE) {
            return item
        }

        parentNode.attachParentedViews(item.nodes)
        item.routingAction.execute()
        return item.withActivationState(globalActivationLevel)
    }

    private fun Node<*>.attachParentedViews(nodes: List<Node.Descriptor>) {
        nodes.forEach { child ->
            if (child.viewAttachMode == Node.AttachMode.PARENT && !child.node.isAttachedToView) {
                attachChildView(child.node)
            }
        }
    }

    override fun <C : Parcelable> transitionElements(item: Resolved<C>, params: ActionExecutionParams<C>): List<TransitionElement<C>> {
        val (_, parentNode, globalActivationLevel) = params

        if (globalActivationLevel != ACTIVE) {
            return emptyList()
        }

        // Don't execute activation twice
        if (item.activationState == ACTIVE) {
            return emptyList()
        }

        return createTransitionElements(parentNode, item)
    }

    private fun <C : Parcelable> createTransitionElements(parentNode: Node<*>, item: Resolved<C>): List<TransitionElement<C>> =
        item.nodes.map { child ->
            transitionElement(child, parentNode, item)
        }.filterNotNull()

    private fun <C : Parcelable> transitionElement(
        child: Node.Descriptor,
        parentNode: Node<*>,
        item: Resolved<C>
    ): TransitionElement<C>? =
        if (child.viewAttachMode == Node.AttachMode.PARENT && !child.node.isAttachedToView) {
            val parentViewGroup = parentNode.targetViewGroupForChild(child.node)
            parentNode.createChildView(child.node)

            child.node.view?.let {
                TransitionElement.Enter(
                    configuration = item.configuration,
                    parentViewGroup = parentViewGroup,
                    identifier = child.node.identifier,
                    view = it.androidView,
                    direction = TransitionDirection.Enter.JustCreated // TODO this can be known by isAcrossLifecycleBarrier
                ) as TransitionElement<C>
            }
        } else null
}
