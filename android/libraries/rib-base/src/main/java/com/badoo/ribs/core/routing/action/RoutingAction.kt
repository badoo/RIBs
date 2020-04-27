package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.customisation.RibCustomisationDirectory

interface RoutingAction {

    fun buildNodes(buildContexts: List<BuildContext>): List<Node<*>> =
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


