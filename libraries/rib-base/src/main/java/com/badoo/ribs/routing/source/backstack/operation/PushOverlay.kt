package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack

/**
 * Operation:
 *
 * [A, B, C] + PushOverlay(O3) = [A, B, C {O3}]
 */
data class PushOverlay<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {

    override fun isApplicable(backStack: BackStack<C>): Boolean =
        backStack.isNotEmpty() && configuration != backStack.currentOverlay

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        backStack.replaceLastWith(
            backStack.last().copy(
                overlays = backStack.last().overlays + Routing(configuration)
            )
        )

    private val BackStack<C>.current: RoutingHistoryElement<C>?
        get() = this.lastOrNull()

    private val BackStack<C>.currentOverlay: C?
        get() = current?.overlays?.lastOrNull()?.configuration

    private fun BackStack<C>.replaceLastWith(replacement: RoutingHistoryElement<C>): BackStack<C> =
        toMutableList().apply { set(lastIndex, replacement) }
}

fun <C : Parcelable> BackStackFeature<C>.pushOverlay(configuration: C) {
    accept(BackStackFeature.Operation(PushOverlay(configuration)))
}
