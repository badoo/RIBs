package com.badoo.ribs.core.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.source.backstack.BackStack

interface BackStackOperation<C : Parcelable> : (BackStack<C>) -> BackStack<C> {
    fun isApplicable(backStack: BackStack<C>): Boolean
}

