package com.badoo.ribs.routing.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

interface Resolution {

    val numberOfNodes: Int

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
        fun noop(): Resolution = object : Resolution {
            override val numberOfNodes: Int = 0
        }
    }
}


