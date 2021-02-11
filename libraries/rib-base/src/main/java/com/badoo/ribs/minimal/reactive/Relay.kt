package com.badoo.ribs.minimal.reactive

import com.badoo.ribs.minimal.reactive.Cancellable.Companion.cancellableOf


class Relay<T> : Source<T>, Emitter<T> {
    private val listeners: MutableList<(T) -> Unit> = mutableListOf()

    override fun emit(value: T) {
        listeners.forEach { it.invoke(value) }
    }

    override fun observe(callback: (T) -> Unit): Cancellable {
        listeners += callback
        return cancellableOf { listeners -= callback }
    }
}
