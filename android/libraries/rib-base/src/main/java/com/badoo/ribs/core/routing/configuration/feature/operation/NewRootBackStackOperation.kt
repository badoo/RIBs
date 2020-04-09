package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature.Operation

data class NewRootBackStackOperation<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: List<BackStackElement<C>>): Boolean =
        backStack.size != 1 || backStack.first().configuration != configuration

    override fun modifyStack(backStack: List<BackStackElement<C>>): List<BackStackElement<C>> =
        listOf(BackStackElement(configuration))
}

fun <C : Parcelable, Content : C> Router<C, *, Content, *, *>.newRoot(configuration: Content) {
    acceptOperation(NewRootBackStackOperation(configuration))
}

internal fun <C : Parcelable> NewRoot(configuration: C) =
    Operation.ExtendedOperation(NewRootBackStackOperation(configuration))
