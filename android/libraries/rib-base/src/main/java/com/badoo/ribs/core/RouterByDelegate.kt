package com.badoo.ribs.core

import android.os.Parcelable
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.RoutingSource
import com.badoo.ribs.core.routing.configuration.ConfigurationResolver

class RouterByDelegate<T : Parcelable>(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<T>,
    private val configurationResolver: ConfigurationResolver<T>
): Router<T>(
    buildParams = buildParams,
    routingSource = routingSource
), ConfigurationResolver<T> by configurationResolver
