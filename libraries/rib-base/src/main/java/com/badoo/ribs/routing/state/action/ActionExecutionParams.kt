package com.badoo.ribs.routing.state.action

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.changeset.RoutingCommand

internal data class ActionExecutionParams<C : Parcelable>(
    val transactionExecutionParams: TransactionExecutionParams<C>,
    val command: RoutingCommand<C>,
    val routing: Routing<C>,
    val addedOrRemoved: Boolean
) {
    val item: RoutingContext.Resolved<C> by lazy {
        transactionExecutionParams.resolver.invoke(routing)
    }
}
