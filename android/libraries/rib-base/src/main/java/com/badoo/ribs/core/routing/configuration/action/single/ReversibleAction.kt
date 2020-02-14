package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable

abstract class ReversibleAction<T : Parcelable> : Action<T>{
    protected var isReversed: Boolean = false
        private set

    final override fun reverse() {
        isReversed = !isReversed
        onTransition()
    }
}
