package com.badoo.ribs.core.routing.configuration.feature.operation

import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.*

fun List<TestRouter.Configuration>.asBackStackElements(
    simulateActivation: Boolean = false
): BackStack<TestRouter.Configuration> =
    this.mapIndexed { index, configuration ->
        RoutingHistoryElement(
            routing = Routing(configuration),
            activation = if (simulateActivation && index == lastIndex) ACTIVE else INACTIVE
        )
    }

