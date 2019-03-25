package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.routing.backstack.NodeDescriptor
import com.badoo.ribs.core.view.RibView

interface RoutingAction<V : RibView> {

    fun buildNodes() : List<NodeDescriptor> =
        emptyList()

    fun execute() {

    }

    fun cleanup()  {
    }

    companion object {
        fun <V : RibView> noop(): RoutingAction<V> = object :
            RoutingAction<V> {}
    }
}


