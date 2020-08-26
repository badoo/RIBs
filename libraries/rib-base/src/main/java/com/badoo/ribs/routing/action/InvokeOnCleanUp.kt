package com.badoo.ribs.routing.action

class InvokeOnCleanup(
    private val block: () -> Unit
) : Resolution {

    override val numberOfNodes: Int = 0

    override fun cleanup() {
        block()
    }

    companion object {
        fun cleanup(onLeave: () -> Unit): Resolution =
            InvokeOnCleanup(onLeave)
    }
}
