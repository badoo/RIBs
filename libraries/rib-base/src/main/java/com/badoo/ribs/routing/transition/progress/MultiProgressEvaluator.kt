package com.badoo.ribs.routing.transition.progress

import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.minimal.reactive.map

class MultiProgressEvaluator : ProgressEvaluator {

    private val evaluators = mutableListOf<ProgressEvaluator>()
    private val isAnyPendingStore = ProgressEvaluatorIsAnyPendingStore()

    fun add(evaluator: ProgressEvaluator) {
        evaluators.add(evaluator)
        isAnyPendingStore.add(evaluator)
    }

    override var progress: Float =
        evaluators.map { it.progress }.min() ?: 0f

    override fun isPending(): Boolean =
        evaluators.any { it.isPending() }

    override fun isPendingSource(): Source<Boolean> =
        isAnyPendingStore.map { it.isAnyPending }

}
