package com.badoo.ribs.routing.state.action.single

import android.os.Parcelable
import com.badoo.ribs.routing.state.action.ActionExecutionParams

internal interface ReversibleActionFactory {
    fun <C : Parcelable> create(params: ActionExecutionParams<C>): ReversibleAction<C>
}

