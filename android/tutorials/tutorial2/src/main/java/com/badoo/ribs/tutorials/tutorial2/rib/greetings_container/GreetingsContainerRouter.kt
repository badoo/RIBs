package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter.Configuration
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter.Configuration.Content
import kotlinx.android.parcel.Parcelize

class GreetingsContainerRouter: Router<Configuration, Nothing, Content, Nothing, GreetingsContainerView>(
    initialConfiguration = Content.Default
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<GreetingsContainerView> =
        RoutingAction.noop()
}
