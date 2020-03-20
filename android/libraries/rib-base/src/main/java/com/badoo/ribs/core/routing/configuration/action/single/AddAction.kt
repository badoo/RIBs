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
 * Attaches [Node]s to a parentNode without their views
 */
internal class AddAction<C : Parcelable>(
    private val emitter: EffectEmitter<C>,
    private val key: ConfigurationKey<C>,
    private var item: Resolved<C>,
    private val parentNode: Node<*>
) : Action<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(
            params: ActionExecutionParams<C>,
            actionableNodes: List<Node<*>>
        ): Action<C> {
            return AddAction(
                emitter = params.transactionExecutionParams.emitter,
                key = params.key,
                item = params.item,
                parentNode = params.transactionExecutionParams.parentNode
            )
        }
    }

    override fun onBeforeTransition() {
        parentNode.attachNodes(item.nodes)
        emitter.onNext(
            Effect.Individual.Added(key, item)
        )
    }

    private fun Node<*>.attachNodes(nodes: List<Node.Descriptor>) {
        nodes.forEach {
            attachChildNode(it.node)
        }
    }

    override fun onTransition() {
        emitter.onNext(
            Effect.Individual.PendingRemovalFalse(key)
        )
    }

    override fun onFinish() {
    }

    override val transitionElements: List<TransitionElement<C>> =
        emptyList()
}
