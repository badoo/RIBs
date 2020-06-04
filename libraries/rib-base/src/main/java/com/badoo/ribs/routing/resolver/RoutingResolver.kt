package com.badoo.ribs.routing.resolver

import android.os.Parcelable
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.Routing

interface RoutingResolver<C : Parcelable> {

    fun resolve(routing: Routing<C>): RoutingAction
}
