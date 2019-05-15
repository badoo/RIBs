package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext
import com.badoo.ribs.core.routing.backstack.ConfigurationKey

/**
 * Represents an action that affects multiple configurations when executed.
 */
internal interface MultiConfigurationAction {

    fun execute(pool: Map<ConfigurationKey, ConfigurationContext<*>>, parentNode: Node<*>)

    /**
     * Invokes [block] on all [ConfigurationContext.Resolved] elements that are in the provided [activationState]
     */
    fun Map<ConfigurationKey, ConfigurationContext<*>>.invokeOn(
        activationState: ConfigurationContext.ActivationState,
        block: (ConfigurationContext.Resolved<*>) -> Unit
    ) {
        this
            .filter { it.value is ConfigurationContext.Resolved<*> && it.value.activationState == activationState }
            .map { it.key to it.value as ConfigurationContext.Resolved<*> }
            .forEach { (_, resolved) ->
                block.invoke(resolved)
            }
    }
}
