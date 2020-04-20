package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation

data class PushBackStackOperation<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        configuration != backStack.current?.configuration

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        backStack + BackStackElement(configuration)

    private val BackStack<C>.current: BackStackElement<C>?
        get() = this.lastOrNull()
}

fun <C : Parcelable, Content : C> Router<C, *, Content, *, *>.push(configuration: Content) {
    acceptOperation(PushBackStackOperation(configuration))
}

internal fun <C : Parcelable> Push(configuration: C) =
    Operation(PushBackStackOperation(configuration))
