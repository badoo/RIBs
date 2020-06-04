package com.badoo.ribs.routing.activator

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.Routing

class DefaultChildActivator<C : Parcelable> : ChildActivator<C> {

    override fun activate(routing: Routing<C>, child: Node<*>) {
        requireNotNull(child.parent)
        child.parent.createChildView(child)
        child.parent.attachChildView(child)
    }

    override fun deactivate(routing: Routing<C>, child: Node<*>) {
        requireNotNull(child.parent)
        child.saveViewState()
        child.parent.detachChildView(child)
    }
}
