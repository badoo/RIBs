package com.badoo.ribs.routing.transition.progress

class MultiProgressEvaluator : ProgressEvaluator {

    private val evaluators = mutableListOf<ProgressEvaluator>()

    fun add(evaluator: ProgressEvaluator) {
        evaluators.add(evaluator)
    }

    override var progress: Float =
        evaluators.map { it.progress }.minOrNull() ?: 0f

    override fun isPending(): Boolean =
        evaluators.any { it.isPending() }
}
