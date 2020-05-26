package com.badoo.ribs.core.routing.configuration.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionCallbacks
import com.badoo.ribs.core.routing.configuration.action.TransactionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.single.DeactivateAction
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect
import com.badoo.ribs.core.routing.configuration.feature.WorkingState

/**
 * Calls [DeactivateAction] all elements with an [ActivationState] of [ACTIVE].
 */
internal class SleepAction<C : Parcelable> : MultiConfigurationAction<C> {

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
                parentNode = params.parentNode,
                actionableNodes = configurationContext.nodes,
                isBackStackOperation = false,
                targetActivationState = SLEEPING,
                callbacks = ActionExecutionCallbacks.noop() // FIXME
            )
            action.onBeforeTransition()
            action.onTransition()
            action.onFinish()
        }

        params.emitter.onNext(Effect.Global.Sleep())
        params.emitter.onComplete()
    }
}
