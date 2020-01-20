package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.action.multi.MultiConfigurationAction
import com.badoo.ribs.core.routing.configuration.action.single.ActivateAction
import com.badoo.ribs.core.routing.configuration.action.single.AddAction
import com.badoo.ribs.core.routing.configuration.action.single.DeactivateAction
import com.badoo.ribs.core.routing.configuration.action.single.NoOpAction
import com.badoo.ribs.core.routing.configuration.action.single.RemoveAction
import com.badoo.ribs.core.routing.configuration.action.single.SingleConfigurationAction

/**
 * Represents a command to change one or more [ConfigurationContext] elements.
 *
 * The command object holds only the data necessary to resolve the [ConfigurationContext] that the
 * command needs to be executed on on a logical level.
 *
 * Associated actions that need to be executed (resulting in [RoutingAction] and
 * [Node] manipulations) are to be found in the associated [MultiConfigurationAction] or
 * [SingleConfigurationAction] implementations.
 */
internal sealed class ConfigurationCommand<C : Parcelable> {
    abstract val key: ConfigurationKey
    abstract val actionFactory: ActionFactory

    data class Add<C : Parcelable>(override val key: ConfigurationKey, val configuration: C) : ConfigurationCommand<C>() {
        /**
         * No additional action here, as [AddAction] is executed automatically
         * during [ConfigurationContext.Unresolved.resolve],
         */
        override val actionFactory: ActionFactory =
            NoOpAction.Factory
    }

    data class Activate<C : Parcelable>(override val key: ConfigurationKey) : ConfigurationCommand<C>() {
        override val actionFactory: ActionFactory =
            ActivateAction.Factory
    }

    data class Deactivate<C : Parcelable>(override val key: ConfigurationKey) : ConfigurationCommand<C>() {
        override val actionFactory: ActionFactory =
            DeactivateAction.Factory
    }

    data class Remove<C : Parcelable>(override val key: ConfigurationKey) : ConfigurationCommand<C>() {
        override val actionFactory: ActionFactory =
            RemoveAction.Factory
    }
}
