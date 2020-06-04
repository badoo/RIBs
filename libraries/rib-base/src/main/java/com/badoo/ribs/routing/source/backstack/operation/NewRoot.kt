package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack

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

