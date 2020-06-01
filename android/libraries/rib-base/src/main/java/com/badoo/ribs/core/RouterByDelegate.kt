package com.badoo.ribs.core

import android.os.Parcelable
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.RoutingSource
import com.badoo.ribs.core.routing.activator.ChildActivator
import com.badoo.ribs.core.routing.activator.UnhandledChildActivator
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver

class RouterByDelegate<T : Parcelable>(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<T>,
    private val configurationResolver: ConfigurationResolver<T>,
    clientChildActivator: ChildActivator<T> = UnhandledChildActivator()
): Router<T>(
    buildParams = buildParams,
    routingSource = routingSource,
    clientChildActivator = clientChildActivator
), ConfigurationResolver<T> by configurationResolver
