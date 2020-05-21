package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature

class Remove<C : Parcelable>(
    private val identifier: Routing.Identifier
) : BackStackOperation<C> {

    override fun invoke(backStack: BackStack<C>): BackStack<C> {
        val toRemove = backStack.find { it.routing.identifier == identifier }

        return toRemove?.let { backStack.minus(it) } ?: backStack
    }

    override fun isApplicable(backStack: BackStack<C>): Boolean =
        backStack.find { it.routing.identifier == identifier } != null
}


fun <C : Parcelable> BackStackFeature<C>.remove(identifier: Routing.Identifier) {
    accept(BackStackFeature.Operation(Remove(identifier)))
}
