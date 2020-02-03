package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

/**
 * Represents an action that affects a single [ConfigurationContext.Resolved] configuration when executed.
 */
internal interface SingleConfigurationAction {

    /**
     * @param item   the single configuration to act on
     * @param params execution params holder
     *
     * @return the updated configuration
     */
    fun <C : Parcelable> execute(key: ConfigurationKey, params: ActionExecutionParams<C>): Resolved<C>
}
