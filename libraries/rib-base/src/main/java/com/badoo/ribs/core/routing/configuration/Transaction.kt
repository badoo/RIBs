package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.RoutingCommand.Add
import com.badoo.ribs.core.routing.configuration.RoutingCommand.Remove
import com.badoo.ribs.core.routing.configuration.action.multi.MultiConfigurationAction
import com.badoo.ribs.core.routing.configuration.action.multi.SaveInstanceStateAction
import com.badoo.ribs.core.routing.configuration.action.multi.SleepAction
import com.badoo.ribs.core.routing.configuration.action.multi.WakeUpAction
import com.badoo.ribs.core.routing.configuration.feature.TransitionDescriptor
import com.badoo.ribs.core.routing.history.Routing

internal sealed class Transaction<C : Parcelable> {

    // TODO move these out to top level maybe
    sealed class MultiConfigurationCommand<C : Parcelable> : Transaction<C>() {

        abstract val action: MultiConfigurationAction<C>

        class Sleep<C : Parcelable> : MultiConfigurationCommand<C>() {
            override val action: MultiConfigurationAction<C> =
                SleepAction()
        }

        class WakeUp<C : Parcelable> : MultiConfigurationCommand<C>() {
            override val action: MultiConfigurationAction<C> =
                WakeUpAction()
        }

        class SaveInstanceState<C : Parcelable> : MultiConfigurationCommand<C>() {
            override val action: MultiConfigurationAction<C> =
                SaveInstanceStateAction()
        }
    }

    data class ListOfCommands<C : Parcelable>(
        val descriptor: TransitionDescriptor,
        val commands: List<RoutingCommand<C>>
    ) : Transaction<C>()

    companion object {
        fun <C : Parcelable> from(vararg commands: RoutingCommand<out C>): Transaction<C> =
            ListOfCommands(
                descriptor = TransitionDescriptor.None,
                commands = commands.toList() as List<RoutingCommand<C>>
            )
    }
}

internal fun <C : Parcelable> List<RoutingCommand<C>>.isBackStackOperation(key: Routing<C>): Boolean =
    none { it.key == key && (it is Add || it is Remove) }
