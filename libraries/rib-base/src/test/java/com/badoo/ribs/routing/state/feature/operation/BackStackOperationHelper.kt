package com.badoo.ribs.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryElement
import com.badoo.ribs.routing.history.RoutingHistoryElement.Activation.*
import com.badoo.ribs.routing.source.backstack.Elements

fun List<TestRouter.Configuration>.asBackStackElements(
    simulateActivation: Boolean = false
): Elements<TestRouter.Configuration> =
    this.mapIndexed { index, configuration ->
        RoutingHistoryElement(
            routing = Routing(configuration),
            activation = if (simulateActivation && index == lastIndex) ACTIVE else INACTIVE
        )
    }

