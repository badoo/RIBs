package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature

data class NewRoot<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        backStack.size != 1 || backStack.first().configuration != configuration

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        listOf(BackStackElement(configuration))
}

fun <C : Parcelable, Content : C> Router<C, *, Content, *, *>.newRoot(configuration: Content) {
    acceptOperation(NewRoot(configuration))
}

internal fun <C : Parcelable> newRoot(configuration: C) =
    BackStackFeature.Operation(NewRoot(configuration))
