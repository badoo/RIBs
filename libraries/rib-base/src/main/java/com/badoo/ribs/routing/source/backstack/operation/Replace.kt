package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.Elements

/**
 * Operation:
 *
 * [A, B, C] + Replace(D) = [A, B, D]
 */
data class Replace<C : Parcelable>(
    private val configuration: C
) : BackStack.Operation<C> {

    override fun isApplicable(elements: Elements<C>): Boolean =
        configuration != elements.lastOrNull()?.routing?.configuration

    override fun invoke(elements: Elements<C>): Elements<C> =
        elements.dropLast(1) + RoutingHistoryElement(
            routing = Routing(configuration)
        )
}

fun <C : Parcelable> BackStack<C>.replace(configuration: C) {
    accept(Replace(configuration))
}

