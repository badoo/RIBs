package com.badoo.ribs.example.rib.lorem_ipsum

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumRouter.Configuration
import kotlinx.android.parcel.Parcelize

class LoremIpsumRouter: Router<Configuration, LoremIpsumView>(
    initialConfiguration = Configuration.Default
) {
    override val permanentParts: List<() -> Node<*>> =
        emptyList()

    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<LoremIpsumView> =
        RoutingAction.noop()
}
