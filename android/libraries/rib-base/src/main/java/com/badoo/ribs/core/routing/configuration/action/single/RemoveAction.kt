package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.configuration.feature.EffectEmitter
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Removes [Node]s from their parent, resulting in the end of their lifecycles.
 */
internal class RemoveAction<C : Parcelable>(
    private val emitter: EffectEmitter<C>,
    private val key: ConfigurationKey<C>,
    private var item: Resolved<C>,
    private val params: ActionExecutionParams<C>
) : Action<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            params: ActionExecutionParams<C>,
            actionableNodes: List<Node<*>>
        ): Action<C> =
            RemoveAction(
                emitter = params.transactionExecutionParams.emitter,
                key = params.key,
                item = params.item,
                params = params
            )
    }

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    override fun onBeforeTransition() {
    }

    override fun onTransition() {
        item.nodes.forEach {
            it.node.markPendingDetach(true)
        }
        emitter.onNext(
            Effect.Individual.PendingRemovalTrue(key)
        )
    }

    override fun onFinish() {
        item.nodes.forEach {
            params.transactionExecutionParams.parentNode.detachChildView(it.node)
            params.transactionExecutionParams.parentNode.detachChildNode(it.node)
        }

        emitter.onNext(
            Effect.Individual.Removed(key, item)
        )
    }
}
