package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container

import com.badoo.ribs.core.builder.BuildParams
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.RoutingSource
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial3.rib.hello_world.builder.HelloWorldBuilder
import kotlinx.android.parcel.Parcelize

class GreetingsContainerRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    helloWorldBuilder: HelloWorldBuilder
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        RoutingAction.noop()
}
