package com.badoo.ribs.core.routing.state.action.single

import android.os.Parcelable

internal interface ReversibleAction<C : Parcelable> : RoutingTransitionAction<C> {
    fun reverse()
}
