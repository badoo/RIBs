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
}


sealed class ProgressEvaluator {

    class InProgress : ProgressEvaluator() {
        var progress: Float = 0f
    }

    object Finished : ProgressEvaluator()

    object Processed : ProgressEvaluator()
}
