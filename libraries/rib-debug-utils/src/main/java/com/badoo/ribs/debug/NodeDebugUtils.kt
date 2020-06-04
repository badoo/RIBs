package com.badoo.ribs.debug

import com.badoo.ribs.core.Node
import com.badoo.ribs.util.RIBs

object NodeDebugUtils {

    private const val ARM_RIGHT = "└── "
    private const val INTERSECTION = "├── "
    private const val LINE = "│   "
    private const val SPACE = "    "

    fun printNodeSubtree(node: Node<*>) {
        printNodeSubtree(node, "", true)
    }

    private fun printNodeSubtree(node: Node<*>, prefix: String, isTail: Boolean) {
        RIBs.errorHandler.handleDebugMessage(prefix + (if (isTail) ARM_RIGHT else INTERSECTION) + node.tag)

        val children = node.children

        for (i in 0 until children.size - 1) {
            printNodeSubtree(
                children[i],
                prefix + if (isTail) SPACE else LINE,
                false
            )
        }

        if (node.children.isNotEmpty()) {
            printNodeSubtree(
                node.children.last(),
                prefix + if (isTail) SPACE else LINE,
                true
            )
        }
    }
}
