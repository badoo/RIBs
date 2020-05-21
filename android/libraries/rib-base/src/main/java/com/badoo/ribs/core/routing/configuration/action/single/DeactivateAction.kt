package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.configuration.feature.EffectEmitter
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Detaches views of associated [Node]s to a parentNode, and cleans up the associated [RoutingAction].
 *
 * Will not detach the [Node]s on the logical level, they are kept alive without their views.
 */
internal class DeactivateAction<C : Parcelable>(
    private val emitter: EffectEmitter<C>,
    private val key: ConfigurationKey<C>,
    private var item: Resolved<C>,
    private val parentNode: Node<*>,
    private val actionableNodes: List<Node<*>>,
    private val isBackStackOperation: Boolean,
    private val targetActivationState: ActivationState = INACTIVE
) : Action<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            params: ActionExecutionParams<C>,
            actionableNodes: List<Node<*>>
        ): Action<C> = DeactivateAction(
            emitter = params.transactionExecutionParams.emitter,
            key = params.key,
            item = params.item,
            parentNode = params.transactionExecutionParams.parentNode,
            actionableNodes = actionableNodes,
            isBackStackOperation = params.isBackStackOperation
        )
    }

    override var canExecute: Boolean =
        true

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    override fun onBeforeTransition() {
        transitionElements = actionableNodes.mapNotNull {
            it.view?.let { ribView ->
                TransitionElement(
                    configuration = item.configuration.configuration, // TODO consider passing the whole RoutingElement
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
            actionableNodes.forEach {
                it.markPendingViewDetach(true)
            }
            emitter.onNext(
                Effect.Individual.PendingDeactivateTrue(key)
            )
        }
    }

    override fun onFinish(forceExecute: Boolean) {
        if (canExecute || forceExecute) {
            actionableNodes.forEach {
                it.saveViewState()
                parentNode.detachChildView(it)
            }

            emitter.onNext(
                Effect.Individual.Deactivated(key, item.copy(activationState = targetActivationState))
            )
        }
    }
}
