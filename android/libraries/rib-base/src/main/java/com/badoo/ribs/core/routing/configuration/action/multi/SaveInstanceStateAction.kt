package com.badoo.ribs.core.routing.configuration.action.multi

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

/**
 * Calls saveInstanceState() on all Nodes associated with Resolved configurations in the pool
 */
internal class SaveInstanceStateAction<C : Parcelable> : MultiConfigurationAction<C> {

    /**
     * Finds [Resolved] elements in the pool and maps them to the value returned by calling
     * [Resolved.saveInstanceStace] on them.
     *
     * It's not necessary to handle [ConfigurationContext.Unresolved] elements,
     * as they do not contain any Nodes that could have had a chance to change their states.
     *
     * @return the map of found elements with updated bundles
     */
    override fun execute(
        pool: Map<ConfigurationKey, ConfigurationContext<C>>,
        params: ActionExecutionParams<C>
    ): Map<ConfigurationKey, Resolved<C>> {
        return pool
            .filterValues { it is Resolved<C> }
            .mapValues { (_, value) ->
                (value as Resolved<C>).saveInstanceStace()
            }
    }
}
