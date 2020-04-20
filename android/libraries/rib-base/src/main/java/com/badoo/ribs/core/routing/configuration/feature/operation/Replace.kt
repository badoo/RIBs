package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation

data class Replace<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        configuration != backStack.lastOrNull()?.configuration

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        backStack.dropLast(1) + BackStackElement(configuration)
}

fun <C : Parcelable, Content : C> Router<C, *, Content, *, *>.replace(configuration: Content) {
    acceptOperation(Replace(configuration))
}

internal fun <C : Parcelable> replace(configuration: C) =
    Operation(Replace(configuration))
