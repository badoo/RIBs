package com.badoo.ribs.core.routing.backstack.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.ConfigurationContext
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationKey
import com.badoo.ribs.core.routing.backstack.action.ActionExecutionParams
import com.badoo.ribs.core.routing.backstack.action.single.ActivateAction

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
        pool: Map<ConfigurationKey, ConfigurationContext<C>>,
        params: ActionExecutionParams<C>
    ): Map<ConfigurationKey, ConfigurationContext.Resolved<C>> =
        pool.invokeOn(SLEEPING, params) { foundByFilter ->
            ActivateAction
                .execute(foundByFilter, params)
        }
}
