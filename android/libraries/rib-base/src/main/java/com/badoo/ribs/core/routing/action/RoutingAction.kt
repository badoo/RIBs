package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView

interface RoutingAction<V : RibView> {

    fun buildNodes(bundles: List<Bundle?>) : List<Node.Descriptor> =
        emptyList()

    fun onAttach(nodes: List<Node.Descriptor>) {

    }

    fun onDetach(nodes: List<Node.Descriptor>) {

    }

    fun execute(nodes: List<Node.Descriptor>) {

    }

    fun cleanup(nodes: List<Node.Descriptor>) {
    }

    fun parentNode(): Node<*>? =
        null

    fun anchor(): Node<*>? =
        null

    companion object {
        fun <V : RibView> noop(): RoutingAction<V> = object :
            RoutingAction<V> {}
    }
}


