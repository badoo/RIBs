package com.badoo.ribs.core.routing.configuration.action

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.activator.RoutingActivator
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.feature.EffectEmitter
import com.badoo.ribs.core.routing.history.Routing

/**
 * Helper class for action execution.
 *
 * @param resolver              the resolver in case the pool contains [ConfigurationContext.Unresolved] elements
 * @param parentNode            the [Node] where to attach/detach any child [Node]s during resolution
 * @param globalActivationLevel the global activation level above which activation should not be raised
 */
internal data class TransactionExecutionParams<C : Parcelable>(
    val emitter: EffectEmitter<C>,
    val resolver: (Routing<C>) -> ConfigurationContext.Resolved<C>,
    val activator: RoutingActivator<C>,
    val parentNode: Node<*>,
    val globalActivationLevel: ConfigurationContext.ActivationState
)


internal data class ActionExecutionParams<C : Parcelable>(
    val transactionExecutionParams: TransactionExecutionParams<C>,
    val command: ConfigurationCommand<C>,
    val routing: Routing<C>,
    val isBackStackOperation: Boolean
) {
    val item: ConfigurationContext.Resolved<C> by lazy {
        transactionExecutionParams.resolver.invoke(routing)
    }
}
