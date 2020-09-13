package com.badoo.ribs.routing.resolution

class InvokeOnExecute(
    private val block: () -> Unit
) : Resolution {

    override val numberOfNodes: Int = 0

    override fun execute() {
        block()
    }

    companion object {
        fun execute(onInvoke: () -> Unit): Resolution =
            InvokeOnExecute(onInvoke)
    }
}
