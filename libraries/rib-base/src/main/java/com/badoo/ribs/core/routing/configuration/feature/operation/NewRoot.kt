package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement

data class NewRoot<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {

    //We shouldn't change root if root configuration same but backStack contains overlays
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        !(backStack.size == 1 && backStack.first().routing.configuration == configuration)

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        listOf(RoutingHistoryElement(Routing(configuration)))
}

fun <C : Parcelable> BackStackFeature<C>.newRoot(configuration: C) {
    accept(BackStackFeature.Operation(NewRoot(configuration)))
}

