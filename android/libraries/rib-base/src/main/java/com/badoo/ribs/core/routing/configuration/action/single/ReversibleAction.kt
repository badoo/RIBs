package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.transition.TransitionElement

internal interface ReversibleAction<C : Parcelable> : Action<C> {
    fun reverse()
}
