package com.badoo.ribs.core.routing.transition.progress

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
