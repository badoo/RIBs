package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable

internal sealed class ConfigurationCommand<C : Parcelable> {

    sealed class Global<C : Parcelable> : ConfigurationCommand<C>() {
        class Sleep<C : Parcelable> : Global<C>()
        class WakeUp<C : Parcelable> : Global<C>()
    }

    sealed class Individual<C : Parcelable> : ConfigurationCommand<C>() {
        abstract val key: ConfigurationKey

        data class Add<C : Parcelable>(override val key: ConfigurationKey, val configuration: C) : Individual<C>()
        data class Activate<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>()
        data class Deactivate<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>()
        data class Remove<C : Parcelable>(override val key: ConfigurationKey) : Individual<C>()
    }
}
