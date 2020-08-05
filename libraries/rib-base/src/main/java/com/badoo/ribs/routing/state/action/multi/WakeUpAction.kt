package com.badoo.ribs.routing.state.action.multi

import android.os.Parcelable
import com.badoo.ribs.routing.state.RoutingContext.ActivationState
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.SLEEPING
import com.badoo.ribs.routing.state.action.TransactionExecutionParams
import com.badoo.ribs.routing.state.action.single.ActivateAction
import com.badoo.ribs.routing.state.feature.RoutingStatePool.Effect.Global
import com.badoo.ribs.routing.state.feature.state.WorkingState

/**
 * Calls [ActivateAction] all elements with an [ActivationState] of [SLEEPING].
 */
internal class WakeUpAction<C : Parcelable> : PoolAction<C> {

    /**
     * Filters the pool for [SLEEPING] elements, executes [ActivateAction] on all of them.
     *
     * @return the map of elements updated by [ActivateAction]
     */
    override fun execute(
        state: WorkingState<C>,
        params: TransactionExecutionParams<C>
    ) {
        state.pool.filterByActivationState(SLEEPING, params) { key, configurationContext ->
            val action = ActivateAction(
                emitter = params.emitter,
                item = configurationContext,
                routing = key,
                activator = params.activator,
                parentNode = params.parentNode,
                addedOrRemoved = false,
                globalActivationLevel = params.globalActivationLevel
            )
            action.onBeforeTransition()
            action.onTransition()
            action.onFinish()
        }

        params.emitter.invoke(Global.WakeUp())
    }
}
