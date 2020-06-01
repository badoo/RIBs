package com.badoo.ribs.core.routing.configuration.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.RoutingContext
import com.badoo.ribs.core.routing.configuration.RoutingContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.TransactionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.Pool
import com.badoo.ribs.core.routing.configuration.feature.WorkingState
import com.badoo.ribs.core.routing.history.Routing

/**
 * Represents an action that affects multiple configurations when executed.
 */
internal interface MultiConfigurationAction<C : Parcelable> {

    /**
     * @param pool   pool of all configurations to act on
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
