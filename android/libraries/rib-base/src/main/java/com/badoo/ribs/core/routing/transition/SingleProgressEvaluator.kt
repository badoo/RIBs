package com.badoo.ribs.core.routing.transition

import java.lang.IllegalStateException


interface ProgressEvaluator {
    val progress: Float

    fun isInProgress(): Boolean

    fun isFinished(): Boolean

    fun isProcessed(): Boolean

    fun markProcessed()
}

class MultiProgressEvaluator : ProgressEvaluator {

    private val evaluators = mutableListOf<ProgressEvaluator>()

    fun add(evaluator: ProgressEvaluator) {
        evaluators.add(evaluator)
    }

    override var progress: Float =
        evaluators.map { it.progress }.min() ?: 0f

    override fun isInProgress(): Boolean =
        evaluators.any { it.isInProgress() }

    override fun isFinished(): Boolean =
        evaluators.all { it.isFinished() }

    override fun isProcessed(): Boolean =
        evaluators.all { it.isProcessed() }

    override fun markProcessed() {
        evaluators.forEach {
            it.markProcessed()
        }
    }
}


class SingleProgressEvaluator : ProgressEvaluator {

    var state: Progress = Progress.InProgress()

    override val progress: Float =
        when (val state = state) {
            is Progress.InProgress -> state.progress
            is Progress.Finished -> 1f
            is Progress.Processed -> 1f
        }

    fun updateProgress(progress: Float) {
        when (val state = state) {
            is Progress.InProgress -> state.progress = progress
            else -> throw IllegalStateException("Not in progress anymore")
        }
    }

    override fun isInProgress(): Boolean =
        state is Progress.InProgress

    override fun isFinished(): Boolean =
        state is Progress.Finished

    override fun isProcessed(): Boolean =
        state is Progress.Processed

    fun markFinished() {
        state = Progress.Finished
    }

    override fun markProcessed() {
        state = Progress.Processed
    }

    sealed class Progress {
        class InProgress : Progress() {
            var progress: Float = 0f
        }

        object Finished : Progress()

        object Processed : Progress()
    }

}
