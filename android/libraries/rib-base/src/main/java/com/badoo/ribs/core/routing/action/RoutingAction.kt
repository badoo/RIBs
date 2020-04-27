package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.portal.AncestryInfo

interface RoutingAction {

    fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>) : List<Node<*>> =
        emptyList()

    fun execute() {
    }

    fun cleanup() {
    }

    fun anchor(): Node<*>? =
        null

    companion object {
        fun noop(): RoutingAction = object :
            RoutingAction {}
    }
}


