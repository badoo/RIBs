package com.badoo.ribs.routing.transition.progress

import com.badoo.ribs.minimal.reactive.Source
import com.badoo.ribs.minimal.reactive.distinctUntilChanged
import com.badoo.ribs.minimal.reactive.map
import com.badoo.ribs.minimal.state.Store
import com.badoo.ribs.util.RIBs

class SingleProgressEvaluator : ProgressEvaluator {

    private sealed class Progress {
        object Initialised : Progress()
        object Reset : Progress()
        class InProgress : Progress() {
            var progress: Float = 0f
        }
        object Finished : Progress()
    }

    private val state = object : Store<Progress>(Progress.Initialised) {
        fun setState(state: Progress) {
            emit(state)
        }
    }

    override val progress: Float =
        when (val state = state.state) {
            is Progress.Initialised -> 0f
            is Progress.Reset -> 0f
            is Progress.InProgress -> state.progress
            is Progress.Finished -> 1f
        }

    override val isPending: Boolean
        get() = isInProgress() || isInitialised()

    override val isPendingSource: Source<Boolean> =
        state.map { isPending }.distinctUntilChanged()

    fun start() {
        state.setState(Progress.InProgress())
    }

    fun updateProgress(progress: Float) {
        when (val state = state.state) {
            is Progress.InProgress -> state.progress = progress
            else -> if (progress != 1f && progress != 0f) {
                RIBs.errorHandler.handleNonFatalError(
                    "Progress $progress is not applicable to $state"
                )
            }
        }
    }

    fun reset() {
        state.setState(Progress.Reset)
    }

    fun markFinished() {
        state.setState(Progress.Finished)
    }

    private fun isInProgress(): Boolean =
        state.state is Progress.InProgress

    private fun isInitialised(): Boolean =
        state.state is Progress.Initialised

}
