package com.badoo.ribs.core.routing.action

class InvokeOnExecute(
    private val onInvoke: () -> Unit
) : RoutingAction {

    override fun execute() {
        onInvoke()
    }

    companion object {
        fun execute(onInvoke: () -> Unit): RoutingAction =
            InvokeOnExecute(onInvoke)
    }
}
