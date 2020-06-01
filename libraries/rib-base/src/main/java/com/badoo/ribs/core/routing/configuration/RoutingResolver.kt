package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.history.Routing

interface RoutingResolver<C : Parcelable> {

    fun resolve(routing: Routing<C>): RoutingAction
}
