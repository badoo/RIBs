package com.badoo.ribs.routing.action

class InvokeOnExecute(
    private val block: () -> Unit
) : RoutingAction {

    override val numberOfNodes: Int = 0

    override fun execute() {
        block()
    }

    companion object {
        fun execute(onInvoke: () -> Unit): RoutingAction =
            InvokeOnExecute(onInvoke)
    }
}
