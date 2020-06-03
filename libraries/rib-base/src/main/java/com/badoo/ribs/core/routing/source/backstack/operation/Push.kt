package com.badoo.ribs.core.routing.source.backstack.operation

import android.os.Parcelable
import com.badoo.ribs.core.routing.source.backstack.BackStackFeature
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.badoo.ribs.core.routing.source.backstack.BackStack

data class Push<C : Parcelable>(
    private val configuration: C
) : BackStackOperation<C> {
    override fun isApplicable(backStack: BackStack<C>): Boolean =
        configuration != backStack.current?.configuration

    override fun invoke(backStack: BackStack<C>): BackStack<C> =
        backStack + RoutingHistoryElement(Routing(configuration))

    private val BackStack<C>.current: Routing<C>?
        get() = this.lastOrNull()?.routing
}

fun <C : Parcelable> BackStackFeature<C>.push(configuration: C) {
    accept(BackStackFeature.Operation(Push(configuration)))
}
