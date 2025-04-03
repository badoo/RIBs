package com.badoo.ribs.routing.resolver

import android.os.Parcelable
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution

interface RoutingResolver<C : Parcelable> {

    fun resolve(routing: Routing<C>): Resolution
}
