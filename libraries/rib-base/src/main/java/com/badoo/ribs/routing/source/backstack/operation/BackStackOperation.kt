package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.Elements

interface BackStackOperation<C : Parcelable> : (Elements<C>) -> Elements<C> {
    fun isApplicable(elements: Elements<C>): Boolean
}

