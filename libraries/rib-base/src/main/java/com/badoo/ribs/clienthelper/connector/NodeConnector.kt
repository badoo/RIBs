package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Relay

class NodeConnector<Input, Output>(
    override val input: Relay<Input> = Relay(),
) : Connectable<Input, Output> {

    private val intake = Relay<Output>()
    private val exhaust = Relay<Output>()
    private var isFlushed = false
    private val outputCache = mutableListOf<Output>()

    override val output: Relay<Output> = object : Relay<Output>() {

        override fun emit(value: Output) {
            intake.accept(value)
        }

        override fun observe(callback: (Output) -> Unit): Cancellable =
            exhaust.observe(callback)
    }

    private val cacheSubscription = intake.observe {
        synchronized(this) {
            if (!isFlushed) {
                outputCache.add(it)
            } else {
                exhaust.accept(it)
                switchToExhaust()
            }
        }
    }

    override fun onAttach() {
        synchronized(this) {
            if (isFlushed) error("Already flushed")
            isFlushed = true
            outputCache.forEach { exhaust.accept(it) }
            outputCache.clear()
        }
    }

    private fun switchToExhaust() {
        intake.observe { exhaust.accept(it) }
        cacheSubscription.cancel()
    }
}
