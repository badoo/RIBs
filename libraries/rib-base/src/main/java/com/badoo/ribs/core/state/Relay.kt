package com.badoo.ribs.core.state

import com.badoo.ribs.core.state.Cancellable.Companion.cancellableOf

interface Source<out T> {
    fun observe(callback: (T) -> Unit): Cancellable
}

interface Emittable<in T> {
    fun emit(value: T)
}

interface Cancellable {
    fun cancel()

    companion object {
        fun cancellableOf(f: () -> Unit): Cancellable =
            object : Cancellable {
                override fun cancel() {
                    f()
                }
            }
    }
}

class Relay<T> : Source<T>, Emittable<T> {
    private val listeners: MutableList<(T) -> Unit> = mutableListOf()

    override fun emit(value: T) {
        listeners.forEach { it.invoke(value) }
    }

    override fun observe(callback: (T) -> Unit): Cancellable {
        listeners += callback
        return cancellableOf { listeners -= callback }
    }
}
