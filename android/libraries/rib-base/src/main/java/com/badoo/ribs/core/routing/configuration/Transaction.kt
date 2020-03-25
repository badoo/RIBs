package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.action.multi.MultiConfigurationAction
import com.badoo.ribs.core.routing.configuration.action.multi.SaveInstanceStateAction
import com.badoo.ribs.core.routing.configuration.action.multi.SleepAction
import com.badoo.ribs.core.routing.configuration.action.multi.WakeUpAction
import com.badoo.ribs.core.routing.configuration.feature.TransitionDescriptor

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
        val commands: List<ConfigurationCommand<C>>
    ) : Transaction<C>()

    companion object {
        fun <C : Parcelable> from(vararg commands: ConfigurationCommand<out C>): Transaction<C> =
            ListOfCommands(
                descriptor = TransitionDescriptor.None,
                commands = commands.toList() as List<ConfigurationCommand<C>>
            )
    }
}

internal fun <C : Parcelable> List<ConfigurationCommand<C>>.isBackStackOperation(key: ConfigurationKey<C>): Boolean =
    none { it.key == key && (it is Add || it is Remove) }
