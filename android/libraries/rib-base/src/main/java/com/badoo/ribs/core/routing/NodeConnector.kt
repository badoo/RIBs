package com.badoo.ribs.core.routing

import android.os.Bundle
import com.badoo.ribs.core.Node

interface NodeConnector {
    /**
     * Attaches logical child. Does not imply attaching view.
     */
    fun attachChildNode(childNode: Node<*>, bundle: Bundle? = null)

    /**
     * Attaches child view only. Expectation is that child node should already be attached.
     */
    fun attachChildView(childNode: Node<*>)

    /**
     * Detaches child view only, child node remains alive.
     */
    fun detachChildView(childNode: Node<*>)

    /**
     * Detaches logical child. Does not imply detaching the view, expectation is that it is already detached at this point.
     */
    fun detachChildNode(childNode: Node<*>)

    companion object {
        fun from(
            attachChildNode: (Node<*>, Bundle?) -> Unit,
            attachChildView: (Node<*>) -> Unit,
            detachChildView: (Node<*>) -> Unit,
            detachChildNode: (Node<*>) -> Unit
        ) : NodeConnector =
            object : NodeConnector {
                override fun attachChildNode(childNode: Node<*>, bundle: Bundle?) {
                    attachChildNode.invoke(childNode, bundle)
                }

                override fun attachChildView(childNode: Node<*>) {
                    attachChildView.invoke(childNode)
                }

                override fun detachChildView(childNode: Node<*>) {
                    detachChildView.invoke(childNode)
                }

                override fun detachChildNode(childNode: Node<*>) {
                    detachChildNode.invoke(childNode)
                }
            }
    }
}
