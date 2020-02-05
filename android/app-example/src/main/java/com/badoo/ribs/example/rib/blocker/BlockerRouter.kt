package com.badoo.ribs.example.rib.blocker

import com.badoo.ribs.core.BuildContext
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.blocker.BlockerRouter.Configuration
import kotlinx.android.parcel.Parcelize

class BlockerRouter(
    buildContext: BuildContext<Nothing?>
): Router<Configuration, Nothing, Configuration, Nothing, BlockerView>(
    buildContext = buildContext,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<BlockerView> =
        RoutingAction.noop()
}
