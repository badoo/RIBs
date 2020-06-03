package com.badoo.ribs.core.routing.state.feature.operation

import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.routing.Routing
import com.badoo.ribs.core.routing.history.RoutingHistoryElement
import com.badoo.ribs.core.routing.history.RoutingHistoryElement.Activation.*
import com.badoo.ribs.core.routing.source.backstack.BackStack

fun List<TestRouter.Configuration>.asBackStackElements(
    simulateActivation: Boolean = false
): BackStack<TestRouter.Configuration> =
    this.mapIndexed { index, configuration ->
        RoutingHistoryElement(
            routing = Routing(configuration),
            activation = if (simulateActivation && index == lastIndex) ACTIVE else INACTIVE
        )
    }

