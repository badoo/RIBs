package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.action.ActivateAction
import com.badoo.ribs.core.routing.backstack.action.AddAction
import com.badoo.ribs.core.routing.backstack.action.DeactivateAction
import com.badoo.ribs.core.routing.backstack.action.MultiConfigurationAction
import com.badoo.ribs.core.routing.backstack.action.RemoveAction
import com.badoo.ribs.core.routing.backstack.action.SingleConfigurationAction
import com.badoo.ribs.core.routing.backstack.action.SleepAction
import com.badoo.ribs.core.routing.backstack.action.WakeUpAction

internal sealed class ConfigurationCommand<C : Parcelable> {

    sealed class Global<C : Parcelable> : ConfigurationCommand<C>() {

        abstract val action: MultiConfigurationAction

        class Sleep<C : Parcelable> : Global<C>() {
            override val action: MultiConfigurationAction = SleepAction
        }

        class WakeUp<C : Parcelable> : Global<C>() {
            override val action: MultiConfigurationAction = WakeUpAction
        }
    }

    sealed class Individual<C : Parcelable> : ConfigurationCommand<C>() {
        abstract val key: ConfigurationKey
        abstract val action: SingleConfigurationAction

        data class Add<C : Parcelable>(override val key: ConfigurationKey, val configuration: C) : Individual<C>() {
            override val action: SingleConfigurationAction = AddAction
        }

        data class Activate<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>() {
            override val action: SingleConfigurationAction = ActivateAction
        }

        data class Deactivate<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>() {
            override val action: SingleConfigurationAction = DeactivateAction
        }

        data class Remove<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>() {
            override val action: SingleConfigurationAction = RemoveAction
        }
    }
}
