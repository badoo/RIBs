package com.badoo.ribs.core.state

abstract class Store<State>(
    initialState: State,
    private val relay: Relay<State> = Relay()
) : Source<State> by relay, Cancellable {
    private var isCancelled = false
    var state: State = initialState
        private set

    protected fun emit(value: State) {
        if (isCancelled) return

        this.state = value
        relay.emit(value)
    }

    override fun cancel() {
        isCancelled = true
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
