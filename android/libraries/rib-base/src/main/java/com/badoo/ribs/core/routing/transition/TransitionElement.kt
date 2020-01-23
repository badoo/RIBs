package com.badoo.ribs.core.routing.transition

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import com.badoo.ribs.core.Rib

open class TransitionElement<C>(
    val configuration: C,
    val direction: TransitionDirection,
    val isBackStackOperation: Boolean,
    val parentViewGroup: ViewGroup,
    val identifier: Rib,
    val view: View,
    val progressEvaluator: MultiProgressEvaluator = MultiProgressEvaluator()
) : ProgressEvaluator by progressEvaluator {

//    internal var transitions: MutableList<Transition> = mutableListOf()
}

//operator fun <T> TransitionElement<out T>?.invoke(transition: TransitionElement<out T>.() -> Transition): Transition? =
//    this?.let {
//        transition.invoke(this)
//    }

@CheckResult
operator fun <T> List<TransitionElement<out T>>.invoke(transition: TransitionElement<out T>.() -> Transition): List<Transition> =
    map {
        transition.invoke(it)
    }
