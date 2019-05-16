package com.badoo.ribs.core.routing.backstack.action

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.ConfigurationContext
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationKey

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
        pool: Map<ConfigurationKey, ConfigurationContext<C>>,
        params: ActionExecutionParams<C>
    ): Map<ConfigurationKey, ConfigurationContext.Resolved<C>> =
        pool.invokeOn(ACTIVE, params) { foundByFilter ->
            DeactivateAction
                .execute(foundByFilter, params)
                .withActivationState(SLEEPING)
        }
}
