package com.badoo.ribs.routing.router

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.activator.ChildActivator
import com.badoo.ribs.routing.activator.UnhandledChildActivator
import com.badoo.ribs.routing.resolver.RoutingResolver
import com.badoo.ribs.routing.source.RoutingSource

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
