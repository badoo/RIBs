package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

internal interface ActionFactory {
    fun <C : Parcelable> create(params: ActionExecutionParams<C>): Action<C>
}

