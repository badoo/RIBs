package com.badoo.ribs.core.routing.backstack.action

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext

internal interface SingleConfigurationAction {

    fun execute(item: ConfigurationContext.Resolved<*>, parentNode: Node<*>)
}
