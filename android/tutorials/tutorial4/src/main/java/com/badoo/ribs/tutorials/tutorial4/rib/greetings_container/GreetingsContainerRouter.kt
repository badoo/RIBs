package com.badoo.ribs.tutorials.tutorial4.rib.greetings_container

import android.os.Parcelable
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.tutorials.tutorial4.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial4.rib.hello_world.builder.HelloWorldBuilder
import kotlinx.android.parcel.Parcelize

class GreetingsContainerRouter(
    private val helloWorldBuilder: HelloWorldBuilder
): Router<Configuration, GreetingsContainerView>(
    initialConfiguration = Configuration.Default
) {
    override val permanentParts: List<() -> Node<*>> = listOf(
        { helloWorldBuilder.build() }
    )

    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<GreetingsContainerView> =
        RoutingAction.noop()

    override fun getParentViewForChild(child: Rib, view: GreetingsContainerView?): ViewGroup? =
        view!!.childContainer
}
