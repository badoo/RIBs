package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.view.TransitionDirection
import com.badoo.ribs.core.view.TransitionElement

/**
 * Detaches views of associated [Node]s to a parentNode, and cleans up the associated [RoutingAction].
 *
 * Will not detach the [Node]s on the logical level, they are kept alive without their views.
 */
internal object DeactivateAction : ResolvedSingleConfigurationAction() {

    override fun <C : Parcelable> execute(item: Resolved<C>, params: ActionExecutionParams<C>): Resolved<C> {
        val (_, parentNode, _) = params
        item.routingAction.cleanup()
        item.nodes.saveViewState()
        parentNode.detachChildViews(item.nodes)

        return item.withActivationState(INACTIVE)
    }

    private fun List<Node.Descriptor>.saveViewState() {
        forEach {
            it.node.saveViewState()
        }
    }

    private fun Node<*>.detachChildViews(nodes: List<Node.Descriptor>) {
        nodes.forEach {
            if (it.viewAttachMode == Node.AttachMode.PARENT) {
                detachChildView(it.node)
            }
        }
    }

    override fun <C : Parcelable> transitionElements(item: Resolved<C>, params: ActionExecutionParams<C>): List<TransitionElement<C>> {
        val (_, parentNode, globalActivationLevel) = params

        if (globalActivationLevel != ConfigurationContext.ActivationState.ACTIVE) {
            return emptyList()
        }

        // Don't execute activation twice
        if (item.activationState == ConfigurationContext.ActivationState.ACTIVE) {
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

            child.node.view?.let {
                TransitionElement.Exit(
                    configuration = item.configuration,
                    parentViewGroup = parentViewGroup,
                    identifier = child.node.identifier,
                    view = it.androidView,
                    direction = TransitionDirection.Exit.Destroyed // TODO this can be known by isAcrossLifecycleBarrier
                ) as TransitionElement<C>
            }
        } else null
}
