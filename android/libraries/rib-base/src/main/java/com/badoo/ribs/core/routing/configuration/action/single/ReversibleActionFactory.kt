package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.configuration.action.TransactionExecutionParams

internal interface ReversibleActionFactory {
    fun <C : Parcelable> create(params: ActionExecutionParams<C>): ReversibleAction<C>
}

