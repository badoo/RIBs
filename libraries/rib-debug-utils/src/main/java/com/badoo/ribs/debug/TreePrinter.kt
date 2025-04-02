package com.badoo.ribs.debug

import com.badoo.ribs.core.Node
import com.badoo.ribs.util.RIBs

object TreePrinter {

    val FORMAT_SIMPLE: (Node<*>) -> String = { it.javaClass.simpleName }
    val FORMAT_SIMPLE_INSTANCE: (Node<*>) -> String =
        { "${it.javaClass.simpleName} @${System.identityHashCode(it)}" }

    private const val ACTIVE = "* "
    private const val ARM_RIGHT = "└── "
    private const val INTERSECTION = "├── "
    private const val LINE = "│   "
    private const val SPACE = "    "

    fun printNodeSubtree(
        node: Node<*>,
        toString: (Node<*>) -> String = FORMAT_SIMPLE_INSTANCE
    ) {
        printNodeSubtree(node, toString, "", true)
    }

    private fun printNodeSubtree(
        node: Node<*>,
        toString: (Node<*>) -> String,
        prefix: String,
        isTail: Boolean
    ) {
        val structurePrefix = when {
            node.isRoot -> ""
            isTail -> ARM_RIGHT
            else -> INTERSECTION
        }
        val active = when {
            node.isActive -> ACTIVE
            else -> ""
        }
        RIBs.errorHandler.handleDebugMessage("$prefix$structurePrefix$active${toString.invoke(node)}")

        val children = node.children
        node.children.forEachIndexed { idx, child ->
            printNodeSubtree(
                child,
                toString,
                prefix + if (isTail) SPACE else LINE,
                idx == children.lastIndex
            )
        }
    }
}
