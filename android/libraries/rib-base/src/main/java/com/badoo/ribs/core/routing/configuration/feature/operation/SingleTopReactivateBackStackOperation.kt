package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement

internal data class SingleTopReactivateBackStackOperation<C : Parcelable>(
    private val configuration: C,
    private val position: Int
) : BackStackOperation<C> {

    override fun isApplicable(backStack: List<BackStackElement<C>>): Boolean =
        position != -1 && backStack[position] == configuration

    override fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
        backStack.dropLast(backStack.size - position - 1)
}
