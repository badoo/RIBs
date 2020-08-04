package com.badoo.ribs.core.state

internal abstract class Store<State>(
    initialState: State
) : Source<State>, Cancellable {
    private val initThread = Thread.currentThread()

    private var isCancelled = false
    var state: State = initialState
        private set

    private val relay: Relay<State> = Relay()

    protected fun emit(value: State) {
        if (isCancelled) return

        if (initThread != Thread.currentThread()) {
            throw SameThreadVerificationException(
                "Emit should be called on the same thread on which store is initialized. " +
                    "Current: ${Thread.currentThread().name}, initial: ${initThread.name}."
            )
        }

        this.state = value
        relay.emit(value)
    }

    override fun cancel() {
        isCancelled = true
    }

    override fun observe(callback: (State) -> Unit): Cancellable {
        callback(state)
        return relay.observe(callback)
    }
}

internal abstract class AsyncStore<Event, State>(initialState: State) : Store<State>(initialState) {
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
