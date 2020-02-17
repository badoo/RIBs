package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
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
    private val params: ActionExecutionParams<C>,
    private val isBackStackOperation: Boolean
) : ReversibleAction<C>() {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(key: ConfigurationKey, params: ActionExecutionParams<C>, isBackStackOperation: Boolean): Action<C> {
            val item = params.resolver.invoke(key)
            return DeactivateAction(item, params, isBackStackOperation)
        }
    }

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    private val actionableNodes = item.nodes
        .filter { it.viewAttachMode == AttachMode.PARENT && it.isAttachedToView }

    override fun onBeforeTransition() {
        transitionElements = actionableNodes.mapNotNull {
            it.view?.let { ribView ->
                TransitionElement(
                    configuration = item.configuration,
                    direction = TransitionDirection.EXIT,
                    isBackStackOperation = isBackStackOperation,
                    parentViewGroup = params.parentNode.targetViewGroupForChild(it),
                    identifier = it.identifier,
                    view = ribView.androidView
                )
            }
        }
    }

    override fun onTransition() {
        if (isReversed) {
            item.routingAction.execute()
            actionableNodes.forEach {
                it.markPendingViewDetach(false)
            }
        } else {
            item.routingAction.cleanup()
            actionableNodes.forEach {
                it.saveViewState()
                it.markPendingViewDetach(true)
            }
        }
    }

    override fun onFinish() {
        if (!isReversed) {
            actionableNodes.forEach {
                params.parentNode.detachChildView(it)
            }
        }
    }

    override val result: Resolved<C> =
        item.copy(activationState = INACTIVE)
}
