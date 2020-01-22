package com.badoo.ribs.core.routing.transition

import android.view.View
import android.view.ViewGroup
import com.badoo.ribs.core.Rib

open class TransitionElement<C>(
    val configuration: C,
    val direction: TransitionDirection,
    val isBackStackOperation: Boolean,
    val parentViewGroup: ViewGroup,
    val identifier: Rib,
    val view: View,
    val progressEvaluator: MultiProgressEvaluator = MultiProgressEvaluator()
) : ProgressEvaluator by progressEvaluator

operator fun <T> TransitionElement<out T>?.invoke(transition: TransitionElement<out T>.() -> Unit) {
    this?.apply(transition)
}

operator fun <T> List<TransitionElement<out T>>.invoke(transition: TransitionElement<out T>.() -> Unit) {
    forEach {
        it.apply(transition)
    }
}
