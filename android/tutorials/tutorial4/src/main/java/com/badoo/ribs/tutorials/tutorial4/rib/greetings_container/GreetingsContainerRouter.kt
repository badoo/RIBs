package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import com.badoo.ribs.core.builder.BuildParams
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.builder.HelloWorldBuilder
import kotlinx.android.parcel.Parcelize

class GreetingsContainerRouter(
    buildParams: BuildParams<Nothing?>,
    private val helloWorldBuilder: HelloWorldBuilder
): Router<Configuration, Nothing, Configuration, Nothing, Nothing>(
    buildParams = buildParams,
    initialConfiguration = Configuration.HelloWorld
) {
    sealed class Configuration : Parcelable {
        @Parcelize object HelloWorld : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<Nothing> =
        when (configuration) {
            is Configuration.HelloWorld -> attach { helloWorldBuilder.build(it) }
        }
}
