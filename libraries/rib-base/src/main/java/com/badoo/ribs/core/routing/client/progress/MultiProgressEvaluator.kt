package com.badoo.ribs.core.routing.client.progress

class MultiProgressEvaluator : ProgressEvaluator {

    private val evaluators = mutableListOf<ProgressEvaluator>()

    fun add(evaluator: ProgressEvaluator) {
        evaluators.add(evaluator)
    }

    override var progress: Float =
        evaluators.map { it.progress }.min() ?: 0f

    override fun isPending(): Boolean =
        evaluators.any { it.isPending() }
}
