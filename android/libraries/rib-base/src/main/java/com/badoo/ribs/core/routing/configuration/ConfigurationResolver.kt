package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.view.RibView

interface ConfigurationResolver<C : Parcelable, V : RibView> {
    fun resolveConfiguration(configuration: C): RoutingAction<V>
}
