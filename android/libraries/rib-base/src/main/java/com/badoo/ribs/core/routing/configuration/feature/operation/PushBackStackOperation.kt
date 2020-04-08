package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature

data class PushBackStackOperation<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: List<BackStackElement<C>>): Boolean =
        configuration != backStack.current?.configuration

    override fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
        backStack + BackStackElement(configuration)

    private val List<BackStackElement<C>>.current: BackStackElement<C>?
        get() = this.lastOrNull()
}

fun <C : Parcelable, Content : C> Router<C, *, Content, *, *>.push(configuration: Content) {
    acceptOperation(PushBackStackOperation(configuration))
}

internal fun <C : Parcelable> Push(configuration: C) =
    BackStackFeature.Operation.ExtendedOperation(PushBackStackOperation(configuration))