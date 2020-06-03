package com.badoo.ribs.core.routing.state.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.state.RoutingContext.ActivationState
import com.badoo.ribs.core.routing.state.RoutingContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.state.RoutingContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.state.action.TransactionExecutionParams
import com.badoo.ribs.core.routing.state.action.single.DeactivateAction
import com.badoo.ribs.core.routing.state.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.state.feature.WorkingState

/**
 * Calls [DeactivateAction] all elements with an [ActivationState] of [ACTIVE].
 */
internal class SleepAction<C : Parcelable> : PoolAction<C> {

    /**
     * Filters the pool for [ACTIVE] elements, executes [DeactivateAction] on all of them.
     *
     * @return the map of elements updated by [DeactivateAction]
     */
    override fun execute(
        state: WorkingState<C>,
        params: TransactionExecutionParams<C>
    ) {
        state.ongoingTransitions.forEach { it.jumpToEnd() }
        state.pool.filterByActivationState(ACTIVE, params) { key, configurationContext ->
            val action = DeactivateAction(
                emitter = params.emitter,
                item = configurationContext,
                routing = key,
                activator = params.activator,
                parentNode = params.parentNode,
                addedOrRemoved = false,
                targetActivationState = SLEEPING
            )
            action.onBeforeTransition()
            action.onTransition()
            action.onFinish()
        }

        params.emitter.onNext(Effect.Global.Sleep())
        params.emitter.onComplete()
    }
}
