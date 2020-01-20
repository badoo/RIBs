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
    var progressEvaluator: ProgressEvaluator = ProgressEvaluator.DEFAULT

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


interface ProgressEvaluator {

    // @FloatRange(from = 0.0, to = 1.0)
    val progress: Float

    companion object {
        val DEFAULT = object : ProgressEvaluator {
            override val progress: Float =
                1.0f
        }

        val DONE = object : ProgressEvaluator {
            override val progress: Float =
                Float.POSITIVE_INFINITY
        }
    }
}

class MutableProgressEvaluator : ProgressEvaluator {
    private var _progress: Float = 0f
    override val progress: Float
        get() = _progress

    fun setProgress(progress: Float) {
        _progress = progress
    }
}
