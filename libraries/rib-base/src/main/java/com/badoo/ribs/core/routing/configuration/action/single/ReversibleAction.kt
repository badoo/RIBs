package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable

internal interface ReversibleAction<C : Parcelable> : RoutingTransitionAction<C> {
    fun reverse()
}
