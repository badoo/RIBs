package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStack

interface BackStackOperation<C : Parcelable> : (BackStack<C>) -> BackStack<C> {
    fun isApplicable(backStack: BackStack<C>): Boolean
}

