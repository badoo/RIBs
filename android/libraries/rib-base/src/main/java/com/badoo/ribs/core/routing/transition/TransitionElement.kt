package com.badoo.ribs.core.routing.transition

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.routing.transition.progress.MultiProgressEvaluator
import com.badoo.ribs.core.routing.transition.progress.ProgressEvaluator

open class TransitionElement<C>(
    val configuration: C,
    val direction: TransitionDirection,
    val isBackStackOperation: Boolean,
    val parentViewGroup: ViewGroup,
    val identifier: Rib.Identifier,
    val view: View,
    val progressEvaluator: MultiProgressEvaluator = MultiProgressEvaluator()
) : ProgressEvaluator by progressEvaluator


@CheckResult
operator fun <T> List<TransitionElement<out T>>.invoke(transition: TransitionElement<out T>.() -> Transition): List<Transition> =
    map {
        transition.invoke(it)
    }
