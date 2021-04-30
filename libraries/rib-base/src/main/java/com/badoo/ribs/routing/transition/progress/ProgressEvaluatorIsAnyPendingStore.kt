package com.badoo.ribs.routing.transition.progress

import com.badoo.ribs.minimal.reactive.CompositeCancellable
import com.badoo.ribs.minimal.state.AsyncStore
import com.badoo.ribs.routing.transition.progress.ProgressEvaluatorIsAnyPendingStore.Event
import com.badoo.ribs.routing.transition.progress.ProgressEvaluatorIsAnyPendingStore.State

internal class ProgressEvaluatorIsAnyPendingStore(
    evaluators: List<ProgressEvaluator> = emptyList(),
) : AsyncStore<Event, State>(State()) {

    sealed class Event {
        class UpdatePendingValue(val sourceHash: Int, val isPending: Boolean) : Event()
    }

    data class State(
        val pendingValues: Map<Int, Boolean> = emptyMap()
    ) {
        val isAnyPending: Boolean
            get() = pendingValues.values.any { it }
    }

    private val cancellables = CompositeCancellable()

    init {
        evaluators.forEach { add(it) }
    }

    override fun reduceEvent(event: Event, state: State): State =
        when (event) {
            is Event.UpdatePendingValue ->
                state.copy(pendingValues = state.pendingValues + (event.sourceHash to event.isPending))
        }

    fun add(evaluator: ProgressEvaluator) {
        cancellables += evaluator.isPendingSource().observe {
            emitEvent(Event.UpdatePendingValue(sourceHash = evaluator.hashCode(), isPending = it))
        }
    }

    override fun cancel() {
        super.cancel()
        cancellables.cancel()
    }

}
