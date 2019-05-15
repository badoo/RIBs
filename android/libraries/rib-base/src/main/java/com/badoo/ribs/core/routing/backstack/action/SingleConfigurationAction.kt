package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

/**
 * Represents an action that affects a single [ConfigurationContext.Resolved] configuration when executed.
 */
internal interface SingleConfigurationAction {

    fun execute(item: ConfigurationContext.Resolved<*>, parentNode: Node<*>)
}
