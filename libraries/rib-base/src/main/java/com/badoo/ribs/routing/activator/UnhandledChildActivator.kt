package com.badoo.ribs.routing.activator

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.util.RIBs

class UnhandledChildActivator<C : Parcelable> : ChildActivator<C> {

    override fun activate(routing: Routing<C>, child: Node<*>) {
        RIBs.errorHandler.handleNonFatalError("Child requires client code activation! Routing: $routing, child: $child")
    }

    override fun deactivate(routing: Routing<C>, child: Node<*>) {
        RIBs.errorHandler.handleNonFatalError("Child requires client code deactivation! Routing: $routing, child: $child")
    }
}
