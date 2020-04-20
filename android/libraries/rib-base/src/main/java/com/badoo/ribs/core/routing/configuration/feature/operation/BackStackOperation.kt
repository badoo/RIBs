package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement

interface BackStackOperation<C : Parcelable> : (BackStack<C>) -> BackStack<C> {
    fun isApplicable(backStack: BackStack<C>): Boolean
}

typealias BackStack<C> = List<BackStackElement<C>>
