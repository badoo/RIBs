package com.badoo.ribs.core.routing.configuration.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.TransactionExecutionParams
import com.badoo.ribs.core.routing.configuration.feature.WorkingState

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
//        : Map<ConfigurationKey, Resolved<C>>

    /**
     * Invokes [block] on all [ConfigurationContext.Resolved] elements that are in the provided [filterActivationState]
     */
    fun Map<ConfigurationKey, ConfigurationContext<C>>.invokeOn(
        filterActivationState: ConfigurationContext.ActivationState,
        params: TransactionExecutionParams<C>,
        block: (ConfigurationKey, Resolved<C>) -> Unit
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
