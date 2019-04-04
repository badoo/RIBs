package com.badoo.ribs.core.routing.backstack

import com.badoo.ribs.core.Node

data class NodeDescriptor(
    val node: Node<*>,
    val viewAttachMode: Node.ViewAttachMode
)
