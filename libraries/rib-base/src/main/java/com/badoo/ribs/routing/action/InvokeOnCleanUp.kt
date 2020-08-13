package com.badoo.ribs.routing.action

class InvokeOnCleanup(
    private val block: () -> Unit
) : RoutingAction {

    override val numberOfNodes: Int = 0

    override fun cleanup() {
        block()
    }

    companion object {
        fun cleanup(onLeave: () -> Unit): RoutingAction =
            InvokeOnCleanup(onLeave)
    }
}
