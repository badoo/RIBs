package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.configuration.Action
import com.badoo.ribs.core.routing.configuration.ActionFactory
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * Removes [Node]s from their parent, resulting in the end of their lifecycles.
 */
internal class RemoveAction<C : Parcelable>(
    private var item: Resolved<C>,
    private val params: ActionExecutionParams<C>
) : Action<C> {

    object Factory: ActionFactory {
        override fun <C : Parcelable> create(key: ConfigurationKey, params: ActionExecutionParams<C>): Action<C> {
            val item = params.resolver.invoke(key)
            return RemoveAction(item, params)
        }
    }

    override var transitionElements: List<TransitionElement<C>> =
        emptyList()

    override fun onBeforeTransition() {
    }

    override fun onTransition() {
    }

    override fun onPostTransition() {
    }

    override fun onFinish() {
        item.nodes.forEach {
            params.parentNode.detachChildView(it.node)
            params.parentNode.detachChildNode(it.node)
        }
    }

    override val result: Resolved<C> =
        item
}
