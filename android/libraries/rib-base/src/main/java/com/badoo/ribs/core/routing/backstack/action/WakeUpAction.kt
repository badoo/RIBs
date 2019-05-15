package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.*
import com.badoo.ribs.core.routing.backstack.ConfigurationKey

internal object WakeUpAction : MultiConfigurationAction {

    override fun execute(pool: Map<ConfigurationKey, ConfigurationContext<*>>, parentNode: Node<*>) {
        pool.invokeOn(SLEEPING) {
            ActivateAction.execute(it, parentNode)
        }
    }
}
