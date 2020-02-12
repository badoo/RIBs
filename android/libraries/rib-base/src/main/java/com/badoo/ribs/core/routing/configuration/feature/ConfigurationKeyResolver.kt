package com.badoo.ribs.core.routing.configuration.feature

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
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
    ): ConfigurationContext.Resolved<C> {
        val item = state.pool[key] ?: defaultElements[key] ?: error("Key $key was not found in pool: $state.pool")

        return resolveAndAddIfNeeded(item)
    }

    fun resolveAndAddIfNeeded(context: ConfigurationContext<C>): ConfigurationContext.Resolved<C> =
        context.resolve(configurationResolver, parentNode) {
            /**
             * Resolution involves building the associated [Node]s, which need to be guaranteed
             * to be added to the parentNode.
             *
             * Because of this, we need to make sure that [AddAction] is executed every time
             * we resolve, even when no explicit [Add] command was asked.
             *
             * This is to cover cases e.g. when restoring from Bundle:
             * we have a list of [Unresolved] elements that will be resolved on next command
             * (e.g. [WakeUp] / [Activate]), by which time they will need to have been added.
             *
             * [Add] is only called explicitly with direct back stack manipulation, but not on
             * state restoration.
             */
            val action = AddAction(it, parentNode)
            action.onTransition()
            action.result
        }
}
