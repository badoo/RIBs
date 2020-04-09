package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation

data class ReplaceBackStackOperation<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: List<BackStackElement<C>>): Boolean =
        configuration != backStack.lastOrNull()?.configuration

    override fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
        backStack.dropLast(1) + BackStackElement(configuration)
}

fun <C : Parcelable, Content : C> Router<C, *, Content, *, *>.replace(configuration: Content) {
    acceptOperation(ReplaceBackStackOperation(configuration))
}

internal fun <C : Parcelable> Replace(configuration: C) =
    Operation.ExtendedOperation(ReplaceBackStackOperation(configuration))
