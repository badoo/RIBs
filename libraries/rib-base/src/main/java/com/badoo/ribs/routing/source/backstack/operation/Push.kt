package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.Elements

/**
 * Operation:
 *
 * [A, B, C] + Push(D) = [A, B, C, D]
 */
data class Push<C : Parcelable>(
    private val configuration: C
) : BackStack.Operation<C> {

    override fun isApplicable(elements: Elements<C>): Boolean =
        configuration != elements.current?.configuration

    override fun invoke(elements: Elements<C>): Elements<C> =
        elements + RoutingHistoryElement(Routing(configuration))

    private val Elements<C>.current: Routing<C>?
        get() = this.lastOrNull()?.routing
}

fun <C : Parcelable> BackStack<C>.push(configuration: C) {
    accept(Push(configuration))
}
