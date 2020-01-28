package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.transition.ProgressEvaluator
import com.badoo.ribs.core.routing.transition.SingleProgressEvaluator
import com.badoo.ribs.core.routing.transition.TransitionElement

internal interface Action<C : Parcelable> {
    fun onBeforeTransition()
    fun onTransition()
    fun onPostTransition()
    fun onFinish()
    fun reverse()
    val result: ConfigurationContext.Resolved<C>
    val transitionElements: List<TransitionElement<C>>
}
