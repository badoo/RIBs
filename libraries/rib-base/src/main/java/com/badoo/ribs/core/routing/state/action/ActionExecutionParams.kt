package com.badoo.ribs.core.routing.state.action

import android.os.Parcelable
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.state.RoutingContext
import com.badoo.ribs.core.routing.state.transaction.RoutingCommand

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
