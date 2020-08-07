package com.badoo.ribs.core.state

import com.badoo.ribs.core.state.Cancellable.Companion.cancellableOf
import java.util.concurrent.CopyOnWriteArrayList

interface Source<out T> {
    fun observe(callback: (T) -> Unit): Cancellable
}

interface Emitter<in T> {
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

    object Empty : Cancellable {
        override fun cancel() { }
    }
}

class CompositeCancellable(vararg cancellables: Cancellable) : Cancellable {
    private val items = mutableListOf(*cancellables)
    operator fun plusAssign(item: Cancellable) {
        items += item
    }

    override fun cancel() {
        items.forEach { it.cancel() }
        items.clear()
    }
}


internal class Relay<T> : Source<T>, Emitter<T> {
    private val listeners: MutableList<(T) -> Unit> = CopyOnWriteArrayList()

    override fun emit(value: T) {
        listeners.forEach { it.invoke(value) }
    }

    override fun observe(callback: (T) -> Unit): Cancellable {
        listeners += callback
        return cancellableOf { listeners -= callback }
    }

    class Behavior<T>(
        initialValue: T? = null,
        private val internal: Relay<T> = Relay()
    ): Source<T>, Emitter<T> {
        var value: T? = initialValue
            private set

        override fun emit(value: T) {
            this.value = value
            internal.emit(value)
        }

        override fun observe(callback: (T) -> Unit): Cancellable {
            val currentValue = value
            if (currentValue != null) {
                callback(currentValue)
            }
            return internal.observe(callback)
        }
    }
}
