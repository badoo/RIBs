package com.badoo.ribs.tutorials.tutorial5.rib.greetings_container

import android.os.Parcelable
import com.badoo.ribs.core.routing.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.tutorials.tutorial5.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial5.rib.hello_world.builder.HelloWorldBuilder
import kotlinx.android.parcel.Parcelize

class GreetingsContainerRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val helloWorldBuilder: HelloWorldBuilder
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {
    sealed class Configuration : Parcelable {
        @Parcelize object HelloWorld : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
            is Configuration.HelloWorld -> attach { helloWorldBuilder.build(it) }
        }
}
