package com.badoo.ribs.core.state

import com.badoo.ribs.core.state.Cancellable.Companion.cancellableOf

internal interface Source<out T> {
    fun observe(callback: (T) -> Unit): Cancellable
}

internal interface Emitter<in T> {
    fun emit(value: T)
}

internal interface Cancellable {
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

internal class Relay<T> : Source<T>, Emitter<T> {
    private val listeners: MutableList<(T) -> Unit> = mutableListOf()

    override fun emit(value: T) {
        listeners.forEach { it.invoke(value) }
    }

    override fun observe(callback: (T) -> Unit): Cancellable {
        listeners += callback
        return cancellableOf { listeners -= callback }
    }
}
