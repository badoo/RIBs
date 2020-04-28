package com.badoo.ribs.core.routing.action

class InvokeOnCleanup(
    private val f: () -> Unit
) : RoutingAction {

    override val nbNodesToBuild: Int = 0

    override fun cleanup() {
        f()
    }

    companion object {
        fun cleanup(onLeave: () -> Unit): RoutingAction =
            InvokeOnCleanup(onLeave)
    }
}
