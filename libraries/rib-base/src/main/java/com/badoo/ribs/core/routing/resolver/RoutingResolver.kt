package com.badoo.ribs.core.routing.resolver

import android.os.Parcelable
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.Routing

interface RoutingResolver<C : Parcelable> {

    fun resolve(routing: Routing<C>): RoutingAction
}
