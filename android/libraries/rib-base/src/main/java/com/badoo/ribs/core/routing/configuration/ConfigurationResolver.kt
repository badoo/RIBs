package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.action.RoutingAction

interface ConfigurationResolver<C : Parcelable> {
    fun resolveConfiguration(configuration: C): RoutingAction
}
