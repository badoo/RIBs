package com.badoo.ribs.core.routing.configuration.action

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.activator.RoutingActivator
import com.badoo.ribs.core.routing.configuration.RoutingCommand
import com.badoo.ribs.core.routing.configuration.RoutingContext
import com.badoo.ribs.core.routing.configuration.feature.EffectEmitter
import com.badoo.ribs.core.routing.history.Routing

/**
 * Helper class for action execution.
 *
 * @param resolver              the resolver in case the pool contains [RoutingContext.Unresolved] elements
 * @param parentNode            the [Node] where to attach/detach any child [Node]s during resolution
 * @param globalActivationLevel the global activation level above which activation should not be raised
 */
internal data class TransactionExecutionParams<C : Parcelable>(
    val emitter: EffectEmitter<C>,
    val resolver: (Routing<C>) -> RoutingContext.Resolved<C>,
    val activator: RoutingActivator<C>,
    val parentNode: Node<*>,
    val globalActivationLevel: RoutingContext.ActivationState
)


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
