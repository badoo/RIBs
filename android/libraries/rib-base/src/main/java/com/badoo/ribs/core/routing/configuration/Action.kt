package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.transition.TransitionElement

internal interface Action<C : Parcelable> {
    fun onPreExecute()
    fun execute()
    fun onPostExecute()
    fun finally()
    val result: ConfigurationContext.Resolved<C>
    val transitionElements: List<TransitionElement<C>>
}
