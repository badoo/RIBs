package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.action.multi.MultiConfigurationAction
import com.badoo.ribs.core.routing.configuration.action.multi.SaveInstanceStateAction
import com.badoo.ribs.core.routing.configuration.action.multi.SleepAction
import com.badoo.ribs.core.routing.configuration.action.multi.WakeUpAction

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
        // TODO pre-fill list of added && removed
//        val exiting: C?,
//        val entering: C?,
        val commands: List<ConfigurationCommand<C>>
    ) : Transaction<C>() {

        fun isAcrossLifecycleBarrier(key: ConfigurationKey): Boolean =
            commands.find { it.key == key && (it is Add || it is Remove) } != null

    }

    companion object {
        fun <C : Parcelable> from(command: ConfigurationCommand<C>): Transaction<C> =
            ListOfCommands(
                listOf(
                    command
                )
            )
    }
}
