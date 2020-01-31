package com.badoo.ribs.core.routing.transition.progress

class MultiProgressEvaluator :
    ProgressEvaluator {

    private val evaluators = mutableListOf<ProgressEvaluator>()

    fun add(evaluator: ProgressEvaluator) {
        evaluators.add(evaluator)
    }

    override var progress: Float =
        evaluators.map { it.progress }.min() ?: 0f

    override fun isInitialised(): Boolean =
        evaluators.all { it.isInitialised() }

    override fun isReset(): Boolean =
        evaluators.all { it.isReset() }

    override fun isInProgress(): Boolean =
        evaluators.any { it.isInProgress() }

    override fun isFinished(): Boolean =
        evaluators.all { it.isFinished() }

    override fun isProcessed(): Boolean =
        evaluators.all { it.isProcessed() }

    override fun isPending(): Boolean =
        evaluators.any { it.isPending() }

    override fun markProcessed() {
        evaluators.forEach {
            it.markProcessed()
        }
    }
}
