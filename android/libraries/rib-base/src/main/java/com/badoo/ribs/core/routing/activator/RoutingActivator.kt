package com.badoo.ribs.core.routing.activator

import android.os.Parcelable
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.history.Routing

class RoutingActivator<C : Parcelable>(
    private val clientActivator: ChildActivator<C>
) {
    private val defaultActivator: ChildActivator<C> =
        DefaultChildActivator()

    fun activate(routing: Routing<C>, nodes: List<Node<*>>) {
        nodes.forEach { child ->
            when (child.attachMode) {
                AttachMode.PARENT -> defaultActivator.activate(routing, child)
                AttachMode.EXTERNAL -> clientActivator.activate(routing, child)
                AttachMode.REMOTE -> Unit // intended to be no-op
            }
        }
    }

    fun deactivate(routing: Routing<C>, nodes: List<Node<*>>) {
        nodes.forEach { child ->
            when (child.attachMode) {
                AttachMode.PARENT -> defaultActivator.deactivate(routing, child)
                AttachMode.EXTERNAL -> clientActivator.deactivate(routing, child)
                AttachMode.REMOTE -> Unit // intended to be no-op
            }
        }
    }
}
