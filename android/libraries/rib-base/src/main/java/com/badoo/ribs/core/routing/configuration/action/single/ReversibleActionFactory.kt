package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

internal interface ReversibleActionFactory {
    fun <C : Parcelable> create(params: ActionExecutionParams<C>): ReversibleAction<C>
}

