package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.transition.TransitionElement

internal interface RoutingTransitionAction<C : Parcelable> {
    fun onBeforeTransition()
    fun onTransition(forceExecute: Boolean = false)
    fun onFinish(forceExecute: Boolean = false)
    val canExecute: Boolean
    val transitionElements: List<TransitionElement<C>>
}
