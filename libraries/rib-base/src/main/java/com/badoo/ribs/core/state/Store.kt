package com.badoo.ribs.core.state

abstract class Store<State>(
    initialState: State
) : Source<State>, Cancellable {
    private val initThread = Thread.currentThread()

    private var isCancelled = false
    private val relay: Relay.Behavior<State> = Relay.Behavior(initialState)

    val state: State get() = relay.value!!

    protected fun emit(value: State) {
        if (isCancelled) return

        verifyThread()

        relay.emit(value)
    }

    private fun verifyThread() {
        if (initThread != Thread.currentThread()) {
            throw SameThreadVerificationException(
                "Store functions should be called on the same thread where store is initialized. " +
                    "Current: ${Thread.currentThread().name}, initial: ${initThread.name}."
            )
        }
    }

    override fun cancel() {
        isCancelled = true
    }

    override fun observe(callback: (State) -> Unit): Cancellable {
        verifyThread()

        return relay.observe(callback)
    }
}

abstract class AsyncStore<Event, State>(initialState: State) : Store<State>(initialState) {
    private val eventRelay = Relay<Event>()
    private val cancellable = eventRelay.observe {
        emit(reduceEvent(it, state))
    }

    protected abstract fun reduceEvent(event: Event, state: State): State

    protected fun emitEvent(event: Event) {
        eventRelay.emit(event)
    }

    override fun cancel() {
        super.cancel()
        cancellable.cancel()
    }
}

class SameThreadVerificationException(message: String) : IllegalStateException(message)
