package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement

interface BackStackOperation<C : Parcelable> {
    fun isApplicable(backStack: List<BackStackElement<C>>): Boolean
    fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>>
}
