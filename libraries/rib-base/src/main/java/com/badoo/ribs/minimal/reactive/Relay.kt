package com.badoo.ribs.minimal.reactive

import com.badoo.ribs.minimal.reactive.Cancellable.Companion.cancellableOf


open class Relay<T> : Source<T>, Emitter<T> {
    private val listeners: MutableList<(T) -> Unit> = mutableListOf()

    // listenersGuard allows listeners removal inside forEach,
    // which is not allowed by default because of ConcurrentModificationException
    private var listenersGuard = false

    // store listeners to remove and remove them later
    private val removeQueue: MutableList<(T) -> Unit> = mutableListOf()

    fun accept(value: T) {
        emit(value)
    }

    override fun emit(value: T) {
        listenersGuard = true
        listeners.forEach { it.invoke(value) }
        listenersGuard = false
        listeners.removeAll(removeQueue)
        removeQueue.clear()
    }

    override fun observe(callback: (T) -> Unit): Cancellable {
        listeners += callback
        return cancellableOf {
            if (listenersGuard) {
                removeQueue.add(callback)
            } else {
                listeners -= callback
            }
        }
    }
}
