package com.badoo.ribs.core.routing.transition

import java.lang.IllegalStateException


// TODO simplify and remove as many as possible
interface ProgressEvaluator {
    val progress: Float

    fun isInitialised(): Boolean

    fun isReset(): Boolean

    fun isInProgress(): Boolean

    fun isFinished(): Boolean

    fun isProcessed(): Boolean

    fun isPending(): Boolean

    fun markProcessed()
}

class MultiProgressEvaluator : ProgressEvaluator {

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
        evaluators.all { it.isPending() }

    override fun markProcessed() {
        evaluators.forEach {
            it.markProcessed()
        }
    }
}


class SingleProgressEvaluator : ProgressEvaluator {

    var state: Progress = Progress.Initialised

    override val progress: Float =
        when (val state = state) {
            is Progress.Initialised -> 0f
            is Progress.Reset -> 0f
            is Progress.InProgress -> state.progress
            is Progress.Finished -> 1f
            is Progress.Processed -> 1f
        }

    fun start() {
        state = Progress.InProgress()
    }

    fun updateProgress(progress: Float) {
        when (val state = state) {
            is Progress.InProgress -> state.progress = progress
            else -> if (progress != 1f && progress != 0f) {
                throw IllegalStateException("Not in progress anymore")
            }
        }
    }

    fun reset() {
        state = Progress.Reset
    }

    override fun isInitialised(): Boolean =
        state is Progress.Initialised

    override fun isReset(): Boolean =
        state is Progress.Reset

    override fun isPending(): Boolean =
        isInProgress() || isInitialised()

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
        object Initialised : Progress()

        object Reset : Progress()

        class InProgress : Progress() {
            var progress: Float = 0f
        }

        object Finished : Progress()

        object Processed : Progress()
    }

}
