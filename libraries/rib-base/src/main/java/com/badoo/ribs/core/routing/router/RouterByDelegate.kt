package com.badoo.ribs.core.routing.router

import android.os.Parcelable
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.activator.ChildActivator
import com.badoo.ribs.core.routing.activator.UnhandledChildActivator
import com.badoo.ribs.core.routing.resolver.RoutingResolver
import com.badoo.ribs.core.routing.source.RoutingSource

class RouterByDelegate<T : Parcelable>(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<T>,
    private val resolver: RoutingResolver<T>,
    clientChildActivator: ChildActivator<T> = UnhandledChildActivator()
): Router<T>(
    buildParams = buildParams,
    routingSource = routingSource,
    clientChildActivator = clientChildActivator
), RoutingResolver<T> by resolver
