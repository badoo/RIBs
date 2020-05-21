package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.configuration.feature.EffectEmitter
import com.badoo.ribs.core.routing.transition.TransitionDirection
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Attaches views of associated [Node]s to a parentNode, and executes the associated [RoutingAction].
 *
 * The [Node]s are expected to be already added to the parentNode on a logical level.
 */
internal class ActivateAction<C : Parcelable>(
    private val emitter: EffectEmitter<C>,
    private val key: ConfigurationKey<C>,
    private var item: Resolved<C>,
    private val parentNode: Node<*>,
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
                key = params.key,
                item = params.item,
                parentNode = params.transactionExecutionParams.parentNode,
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
                Effect.Individual.Activated(key, item.copy(activationState = globalActivationLevel))
            )
        }

        if (canExecute) {
            prepareTransition()
        }
    }

    private fun prepareTransition() {
        actionableNodes.forEach {
            parentNode.createChildView(it)
            parentNode.attachChildView(it)
        }

        transitionElements = actionableNodes.mapNotNull {
            it.view?.let { ribView ->
                TransitionElement(
                    configuration = item.configuration.configuration, // TODO consider passing the whole RoutingElement
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
                Effect.Individual.PendingDeactivateFalse(key)
            )
        }
    }

    override fun onFinish(forceExecute: Boolean) {
    }
}
