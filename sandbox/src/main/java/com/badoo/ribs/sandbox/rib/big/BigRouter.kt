package com.badoo.ribs.sandbox.rib.big

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.sandbox.rib.big.BigRouter.Configuration
import com.badoo.ribs.sandbox.rib.big.BigRouter.Configuration.Permanent
import com.badoo.ribs.sandbox.rib.small.builder.SmallBuilder
import kotlinx.android.parcel.Parcelize

class BigRouter(
    buildParams: BuildParams<Nothing?>,
    private val smallBuilder: SmallBuilder
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = permanent(Permanent.Small)
) {

    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Small : Permanent()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
            Permanent.Small -> attach { smallBuilder.build(it) }
        }
}
