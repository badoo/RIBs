package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Relay

class NodeConnector<Input, Output>(
    override val input: Relay<Input> = Relay(),
    override val output: Relay<Output> = Relay()
): Connectable<Input, Output> {

    private var isUnlocked = false
    private val outputCache = mutableListOf<Output>()
    private val cacheSubscription: Cancellable = output.observe {
        synchronized(this) {
            if (!isUnlocked) {
                outputCache.add(it)
            } else {
                output.accept(it)
            }
        }
    }

    override fun onAttached() {
        synchronized(this) {
            if (isUnlocked) error("Already unlocked")
            isUnlocked = true
            cacheSubscription.cancel()
            outputCache.forEach { output.accept(it) }
            outputCache.clear()
        }
    }
}
