package com.badoo.ribs.routing.state.action.single

import android.os.Parcelable

internal interface ReversibleAction<C : Parcelable> : RoutingTransitionAction<C> {
    fun reverse()
}
