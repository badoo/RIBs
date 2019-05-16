package com.badoo.ribs.core.routing.backstack.action

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.backstack.ConfigurationContext
import com.badoo.ribs.core.routing.backstack.ConfigurationKey

/**
 * Helper class for action execution.
 *
 * @param resolver              the resolver in case the pool contains [ConfigurationContext.Unresolved] elements
 * @param parentNode            the [Node] where to attach/detach any child [Node]s during resolution
 * @param globalActivationLevel the global activation level above which activation should not be raised
 */
internal data class ActionExecutionParams<C : Parcelable>(
    val resolver: (ConfigurationKey) -> ConfigurationContext.Resolved<C>,
    val parentNode: Node<*>,
    val globalActivationLevel: ConfigurationContext.ActivationState
)
