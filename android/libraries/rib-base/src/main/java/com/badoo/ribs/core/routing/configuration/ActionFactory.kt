package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

internal interface ActionFactory {
    fun <C : Parcelable> create(key: ConfigurationKey, params: ActionExecutionParams<C>): Action<C>
}

