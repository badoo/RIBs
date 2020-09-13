package com.badoo.ribs.routing.activator

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.Routing

class DefaultChildActivator<C : Parcelable>(
    private val parentNode: Node<*>
) : ChildActivator<C> {

    override fun activate(routing: Routing<C>, child: Node<*>) {
        parentNode.attachChildView(child)
    }

    override fun deactivate(routing: Routing<C>, child: Node<*>) {
        parentNode.detachChildView(child)
    }
}
