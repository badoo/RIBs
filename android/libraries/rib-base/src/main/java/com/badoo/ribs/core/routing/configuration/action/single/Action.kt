package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.transition.ProgressEvaluator
import com.badoo.ribs.core.routing.transition.TransitionElement

internal interface Action<C : Parcelable> {
    fun onBeforeTransition()
    fun onTransition()
    fun onPostTransition()
    fun onFinish()
    val result: ConfigurationContext.Resolved<C>
    val transitionElements: List<TransitionElement<C>>
}

internal fun List<TransitionElement<*>>.containsInProgress() =
    any { it.progressEvaluator is ProgressEvaluator.InProgress }

internal fun Action<*>.allTransitionsFinished() =
    transitionElements.all { it.progressEvaluator is ProgressEvaluator.Finished }
