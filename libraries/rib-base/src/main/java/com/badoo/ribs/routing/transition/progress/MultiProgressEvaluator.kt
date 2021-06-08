package com.badoo.ribs.routing.transition.progress

import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.minimal.reactive.combineLatest
import com.badoo.ribs.minimal.reactive.defer
import com.badoo.ribs.minimal.reactive.distinctUntilChanged
import com.badoo.ribs.minimal.reactive.just

class MultiProgressEvaluator : ProgressEvaluator {

    private val evaluators = mutableListOf<ProgressEvaluator>()
    private var lock = false

    fun add(evaluator: ProgressEvaluator) {
        // isPendingSource is being observed only after animation start, so it is safe to lock evaluators list
        // if this behaviour is not correct anymore, feel free to remove and re-evaluate approach
        check(!lock) { "Can't add new evaluators when progress observation started" }
        evaluators.add(evaluator)
    }

    override var progress: Float =
        evaluators.minOfOrNull { it.progress } ?: 0f

    override val isPending: Boolean
        get() = evaluators.any { it.isPending }

    override val isPendingSource: Source<Boolean> = defer {
        lock = true
        if (evaluators.isNotEmpty()) {
            combineLatest(evaluators.map { it.isPendingSource }) { isPending }.distinctUntilChanged()
        } else {
            just { isPending }
        }
    }

}
