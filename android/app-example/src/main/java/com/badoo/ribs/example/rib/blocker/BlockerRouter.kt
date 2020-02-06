package com.badoo.ribs.example.rib.blocker

import com.badoo.ribs.core.BuildParams
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.blocker.BlockerRouter.Configuration
import kotlinx.android.parcel.Parcelize

class BlockerRouter(
    buildParams: BuildParams<Nothing?>
): Router<Configuration, Nothing, Configuration, Nothing, BlockerView>(
    buildParams = buildParams,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<BlockerView> =
        RoutingAction.noop()
}
