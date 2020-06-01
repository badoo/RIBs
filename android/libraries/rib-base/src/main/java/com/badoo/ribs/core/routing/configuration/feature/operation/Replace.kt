package com.badoo.ribs.core.routing.configuration.feature.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement

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

