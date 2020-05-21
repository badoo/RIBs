package com.badoo.ribs.core.routing.configuration.feature.operation

import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement

fun List<TestRouter.Configuration>.asBackStackElements(): BackStack<TestRouter.Configuration> =
    this.map {
        RoutingHistoryElement(
            Routing(it)
        )
    }

