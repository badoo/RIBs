package com.badoo.ribs.core.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext

interface RoutingAction {

    val nbNodesToBuild: Int

    /**
     * Guaranteed by framework to receive a list of nbNodesToBuild elements
     */
    fun buildNodes(buildContexts: List<BuildContext>): List<Rib> =
        emptyList()

    fun execute() {
    }

    fun cleanup() {
    }

    fun anchor(): Node<*>? =
        null

    companion object {
        fun noop(): RoutingAction = object : RoutingAction {
            override val nbNodesToBuild: Int = 0
        }
    }
}


