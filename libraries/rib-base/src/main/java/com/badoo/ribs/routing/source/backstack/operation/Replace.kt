package com.badoo.ribs.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.routing.source.backstack.BackStackFeature
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.source.backstack.BackStack

/**
 * Operation:
 *
 * [A, B, C] + Replace(D) = [A, B, D]
 */
data class Replace<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        configuration != backStack.lastOrNull()?.routing?.configuration

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        backStack.dropLast(1) + RoutingHistoryElement(
            routing = Routing(configuration)
        )
}

fun <C : Parcelable> BackStackFeature<C>.replace(configuration: C) {
    accept(BackStackFeature.Operation(Replace(configuration)))
}

