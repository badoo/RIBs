package com.badoo.ribs.routing.state.action.multi

import android.os.Parcelable
import com.badoo.ribs.routing.state.RoutingContext
import com.badoo.ribs.routing.state.RoutingContext.Resolved
import com.badoo.ribs.routing.state.action.TransactionExecutionParams
import com.badoo.ribs.routing.state.Pool
import com.badoo.ribs.routing.state.feature.state.WorkingState
import com.badoo.ribs.routing.Routing

/**
 * Represents an action that affects multiple [Routing] elements in the pool when executed.
 */
internal interface PoolAction<C : Parcelable> {

    /**
     * @param pool   pool of all [Routing] elements to act on
     * @param params execution params holder
     *
     * @return sub-pool of the updated elements
     */
    fun execute(state: WorkingState<C>, params: TransactionExecutionParams<C>)

    /**
     * Invokes [block] on all [RoutingContext.Resolved] elements that are in the provided [filterActivationState]
     */
    fun Pool<C>.filterByActivationState(
        filterActivationState: RoutingContext.ActivationState,
        params: TransactionExecutionParams<C>,
        block: (Routing<C>, Resolved<C>) -> Unit
    ) {
        this
            .filter { it.value.activationState == filterActivationState }
            .mapValues {
                block.invoke(
                    it.key,
                    params.resolver(it.key)
                )
            }
    }
}
