package com.badoo.ribs.tutorials.tutorial2.rib.greetings_container

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.tutorials.tutorial2.rib.greetings_container.GreetingsContainerRouter.Configuration
import kotlinx.android.parcel.Parcelize
import com.badoo.ribs.core.BuildContext

class GreetingsContainerRouter(
    buildContext: BuildContext.Resolved<Nothing?>
): Router<Configuration, Nothing, Configuration, Nothing, Nothing>(
    buildContext = buildContext,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<Nothing> =
        RoutingAction.noop()
}
