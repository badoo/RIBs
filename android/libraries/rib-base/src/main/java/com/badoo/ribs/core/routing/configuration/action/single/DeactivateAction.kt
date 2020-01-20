package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.Action
import com.badoo.ribs.core.routing.configuration.ActionFactory
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Detaches views of associated [Node]s to a parentNode, and cleans up the associated [RoutingAction].
 *
 * Will not detach the [Node]s on the logical level, they are kept alive without their views.
 */
internal class DeactivateAction<C : Parcelable>(
    private var item: Resolved<C>,
    private val params: ActionExecutionParams<C>
) : Action<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(key: ConfigurationKey, params: ActionExecutionParams<C>): Action<C> {
            val item = params.resolver.invoke(key)
            return DeactivateAction(item, params)
        }
    }

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    private val actionableNodes = item.nodes
        .filter { it.viewAttachMode == Node.AttachMode.PARENT && it.node.isAttachedToView }

    override fun onPreExecute() {
        transitionElements = actionableNodes.mapNotNull {
            it.node.view?.let { ribView ->
                TransitionElement(
                    configuration = item.configuration,
                    direction = TransitionDirection.Exit,
                    parentViewGroup = params.parentNode.targetViewGroupForChild(it.node),
                    identifier = it.node.identifier,
                    view = ribView.androidView
                )
            }
        }
    }

    override fun execute() {
        item.routingAction.cleanup()
        actionableNodes.forEach {
            it.node.saveViewState()
        }
    }

    override fun onPostExecute() {
        actionableNodes.forEach {
            params.parentNode.detachChildView(it.node)
        }
    }

    override fun finally() {
        item = item.copy(activationState = INACTIVE)
    }

    override val result: Resolved<C> =
        item
}
