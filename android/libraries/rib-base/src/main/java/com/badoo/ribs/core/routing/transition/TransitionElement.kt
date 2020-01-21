package com.badoo.ribs.core.routing.transition

import android.view.View
import android.view.ViewGroup
import com.badoo.ribs.core.Rib

open class TransitionElement<C>(
    val configuration: C,
    val direction: TransitionDirection,
    val parentViewGroup: ViewGroup,
    val identifier: Rib,
    val view: View
) {
    var progressEvaluator: ProgressEvaluator =
        ProgressEvaluator.Finished

//    data class Enter<C>(
//        override val configuration: C,
//        override val parentViewGroup: ViewGroup,
//        override val identifier: Rib,
//        override val view: View,
//        val direction: TransitionDirection.Enter
//    ) : TransitionElement<C>()
//
//    data class Exit<C>(
//        override val configuration: C,
//        override val parentViewGroup: ViewGroup,
//        override val identifier: Rib,
//        override val view: View,
//        val direction: TransitionDirection.Exit
//    ) : TransitionElement<C>()
}


sealed class ProgressEvaluator {

//    var progress: Float = 0f
//        set(value) {
////            if (hasBeenProcessed()) throw IllegalStateException("Can't set progress after processed")
////            if (hasFinished()) throw IllegalStateException("Can't set progress after finished")
//            field = value
//        }

    class InProgress : ProgressEvaluator() {
        var progress: Float = 0f
    }

    object Finished : ProgressEvaluator()

    object Processed : ProgressEvaluator()


//    companion object {
//        private val PROGRESS_FINISHED = Float.POSITIVE_INFINITY
//        private val PROGRESS_PROCESSED = Float.NaN
//
////        val DONE = object : ProgressEvaluator {
////            override val progress: Float =
////                Float.POSITIVE_INFINITY
////        }
//    }

//    fun hasFinished(): Boolean =
//        progress == PROGRESS_FINISHED
//
//    fun hasBeenProcessed(): Boolean =
//        progress == PROGRESS_PROCESSED
//
//    fun finish() {
//        progress = PROGRESS_FINISHED
//    }
//    internal fun markAsProcessed() {
//        progress = PROGRESS_PROCESSED
//    }
}
