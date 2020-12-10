package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.Elements

/**
 * Operation:
 *
 * [A, B, C] + PushOverlay(O3) = [A, B, C {O3}]
 */
data class PushOverlay<C : Parcelable>(
    private val configuration: C
) : BackStack.Operation<C> {

    override fun isApplicable(elements: Elements<C>): Boolean =
        elements.isNotEmpty() && configuration != elements.currentOverlay

    override fun invoke(elements: Elements<C>): Elements<C> =
        elements.replaceLastWith(
            elements.last().copy(
                overlays = elements.last().overlays + Routing(configuration)
            )
        )

    private val Elements<C>.current: RoutingHistoryElement<C>?
        get() = this.lastOrNull()

    private val Elements<C>.currentOverlay: C?
        get() = current?.overlays?.lastOrNull()?.configuration

    private fun Elements<C>.replaceLastWith(replacement: RoutingHistoryElement<C>): Elements<C> =
        toMutableList().apply { set(lastIndex, replacement) }
}

fun <C : Parcelable> BackStack<C>.pushOverlay(configuration: C) {
    accept(PushOverlay(configuration))
}
