package com.badoo.ribs.minimal.state

import com.badoo.ribs.minimal.reactive.Cancellable
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.minimal.reactive.Source

abstract class Store<State>(
    initialState: State
) : Source<State>, Cancellable {

    private var isCancelled = false
    var state: State = initialState
        private set

    private val relay: Relay<State> = Relay()

    protected fun emit(value: State) {
        if (isCancelled) return

        synchronized(this) {
            this.state = value
            relay.emit(value)
        }
    }

    override fun cancel() {
        isCancelled = true
    }

    override fun observe(callback: (State) -> Unit): Cancellable {
        synchronized(this) {
            callback(state)
            return relay.observe(callback)
        }
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
