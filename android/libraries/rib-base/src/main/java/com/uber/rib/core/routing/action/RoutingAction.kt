package com.uber.rib.core.routing.action

import com.uber.rib.core.RibView
import com.uber.rib.core.Node

interface RoutingAction<V : RibView> {

    fun ribFactories() : List<() -> Node<*>> =
        emptyList()

    fun execute() {

    }

    fun cleanup()  {
    }

    companion object {
        fun <V : RibView> noop(): RoutingAction<V> = object : RoutingAction<V> {}
    }
}


