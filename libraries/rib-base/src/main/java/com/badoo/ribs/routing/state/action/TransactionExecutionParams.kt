package com.badoo.ribs.routing.state.action

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.activator.RoutingActivator
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.feature.EffectEmitter
import com.badoo.ribs.routing.Routing

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


