package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.single.Action
import com.badoo.ribs.core.routing.configuration.action.single.AddAction

internal class ConfigurationKeyResolver<C : Parcelable>(
    private val configurationResolver: (C) -> RoutingAction<*>,
    private val parentNode: Node<*>
) {
    /**
     * Returns a [Resolved] [ConfigurationContext] looked up by [key].
     *
     * A [ConfigurationContext] should be already present in the pool either in already [Resolved],
     * or [Unresolved] form, the latter of which will be resolved on invocation.
     *
     * The only exception when it's acceptable not to already have an element under [key] is
     * when [defaultElement] is not null, used in the case of the [Add] command.
     */
    fun resolve(
        state: WorkingState<C>,
        key: ConfigurationKey,
        defaultElements: Map<ConfigurationKey, ConfigurationContext<C>>
    ): Pair<ConfigurationContext.Resolved<C>, Action<C>?> {
        val item = state.pool[key] ?: defaultElements[key] ?: error("Key $key was not found in pool: $state.pool")

        return resolveAndAddIfNeeded(item)
    }

    fun resolveAndAddIfNeeded(context: ConfigurationContext<C>): Pair<ConfigurationContext.Resolved<C>, Action<C>?> {
        var action: Action<C>? = null
        val resolved = context.resolve(configurationResolver, parentNode) {
            action = AddAction(it, parentNode)
            it
        }
        return resolved to action
    }
}
