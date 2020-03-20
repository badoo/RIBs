package com.badoo.ribs.core.routing.configuration.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
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
        state.pool.invokeOn(ACTIVE, params) { key, foundByFilter ->
            val action = DeactivateAction(
                emitter = params.emitter,
                item = foundByFilter,
                key = key,
                parentNode = params.parentNode,
                actionableNodes = foundByFilter.nodes.map { it.node },
                isBackStackOperation = false,
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
