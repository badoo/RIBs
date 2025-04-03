package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.Elements

data class NewRoot<C : Parcelable>(
    private val configuration: C
) : BackStack.Operation<C> {

    //We shouldn't change root if root configuration same but backStack contains overlays
    override fun isApplicable(elements: Elements<C>): Boolean =
        !(elements.size == 1 && elements.first().routing.configuration == configuration)

    override fun invoke(elements: Elements<C>): Elements<C> =
        listOf(RoutingHistoryElement(Routing(configuration)))
}

fun <C : Parcelable> BackStack<C>.newRoot(configuration: C) {
    accept(NewRoot(configuration))
}

