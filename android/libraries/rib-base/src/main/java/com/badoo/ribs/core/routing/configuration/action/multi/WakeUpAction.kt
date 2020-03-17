package com.badoo.ribs.core.routing.configuration.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.action.TransactionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.single.ActivateAction
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature.Effect.Global
import com.badoo.ribs.core.routing.configuration.feature.WorkingState

/**
 * Calls [ActivateAction] all elements with an [ActivationState] of [SLEEPING].
 */
internal class WakeUpAction<C : Parcelable> : MultiConfigurationAction<C> {

    /**
     * Filters the pool for [SLEEPING] elements, executes [ActivateAction] on all of them.
     *
     * @return the map of elements updated by [ActivateAction]
     */
    override fun execute(
        state: WorkingState<C>,
        params: TransactionExecutionParams<C>
    ) {
        state.pool.invokeOn(SLEEPING, params) { key, foundByFilter ->
            val action = ActivateAction(
                emitter = params.emitter,
                item = foundByFilter,
                key = key,
                parentNode = params.parentNode,
                actionableNodes = foundByFilter.nodes.map { it.node },
                isBackStackOperation = false,
                globalActivationLevel = params.globalActivationLevel
            )
            action.onBeforeTransition()
            action.onTransition()
            action.onFinish()
        }

        params.emitter.onNext(Global.WakeUp())
        params.emitter.onComplete()
    }
}
