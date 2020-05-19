package com.badoo.ribs.core.plugin

import com.badoo.ribs.core.Node

class NodeAwareImpl: NodeAware {
    override lateinit var node: Node<*>
        private set

    override fun init(node: Node<*>) {
        this.node = node
    }
}
