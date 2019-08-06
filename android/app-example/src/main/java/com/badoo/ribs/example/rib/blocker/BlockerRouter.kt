package com.badoo.ribs.example.rib.blocker

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.blocker.BlockerRouter.Configuration
import kotlinx.android.parcel.Parcelize

class BlockerRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Nothing, Configuration, Nothing, BlockerView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<BlockerView> =
        RoutingAction.noop()
}
