package com.badoo.ribs.core.routing.transition

sealed class ProgressEvaluator {

    class InProgress : ProgressEvaluator() {
        var progress: Float = 0f
    }

    object Finished : ProgressEvaluator()

    object Processed : ProgressEvaluator()
}
