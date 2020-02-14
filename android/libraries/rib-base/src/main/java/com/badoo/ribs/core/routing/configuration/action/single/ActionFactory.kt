package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

internal interface ActionFactory {
    fun <C : Parcelable> create(
        key: ConfigurationKey,
        params: ActionExecutionParams<C>,
        isBackStackOperation: Boolean
    ): Action<C>
}

