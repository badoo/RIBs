package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.action.single.ActivateAction
import com.badoo.ribs.core.routing.backstack.action.single.AddAction
import com.badoo.ribs.core.routing.backstack.action.single.DeactivateAction
import com.badoo.ribs.core.routing.backstack.action.multi.MultiConfigurationAction
import com.badoo.ribs.core.routing.backstack.action.single.NoOpAction
import com.badoo.ribs.core.routing.backstack.action.single.RemoveAction
import com.badoo.ribs.core.routing.backstack.action.single.SingleConfigurationAction
import com.badoo.ribs.core.routing.backstack.action.multi.SleepAction
import com.badoo.ribs.core.routing.backstack.action.multi.WakeUpAction

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

    sealed class MultiConfigurationCommand<C : Parcelable> : ConfigurationCommand<C>() {

        abstract val action: MultiConfigurationAction<C>

        class Sleep<C : Parcelable> : MultiConfigurationCommand<C>() {
            override val action: MultiConfigurationAction<C> =
                SleepAction()
        }

        class WakeUp<C : Parcelable> : MultiConfigurationCommand<C>() {
            override val action: MultiConfigurationAction<C> =
                WakeUpAction()
        }
    }

    sealed class SingleConfigurationCommand<C : Parcelable> : ConfigurationCommand<C>() {
        abstract val key: ConfigurationKey
        abstract val action: SingleConfigurationAction

        data class Add<C : Parcelable>(override val key: ConfigurationKey, val configuration: C) : SingleConfigurationCommand<C>() {
            /**
             * No additional action here, as [AddAction] is executed automatically
             * during [ConfigurationContext.Unresolved.resolve],
             */
            override val action: SingleConfigurationAction =
                NoOpAction
        }

        data class Activate<C : Parcelable>(override val key: ConfigurationKey) : SingleConfigurationCommand<C>() {
            override val action: SingleConfigurationAction =
                ActivateAction
        }

        data class Deactivate<C : Parcelable>(override val key: ConfigurationKey) : SingleConfigurationCommand<C>() {
            override val action: SingleConfigurationAction =
                DeactivateAction
        }

        data class Remove<C : Parcelable>(override val key: ConfigurationKey) : SingleConfigurationCommand<C>() {
            override val action: SingleConfigurationAction =
                RemoveAction
        }
    }
}
