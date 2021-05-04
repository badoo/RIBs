package com.badoo.ribs.rx2.clienthelper.connector

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.disposables.Disposable

class NodeConnector<Input, Output>(
    override val input: Relay<Input> = PublishRelay.create(),
    override val output: Relay<Output> = PublishRelay.create()
) : Connectable<Input, Output> {

    private var isUnlocked = false
    private val outputCache = mutableListOf<Output>()
    private val cacheSubscription: Disposable = output.subscribe {
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
            cacheSubscription.dispose()
            outputCache.forEach { output.accept(it) }
            outputCache.clear()
        }
    }
}
