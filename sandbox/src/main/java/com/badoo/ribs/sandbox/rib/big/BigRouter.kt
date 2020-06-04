package com.badoo.ribs.sandbox.rib.big

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.sandbox.rib.big.BigRouter.Configuration
import com.badoo.ribs.sandbox.rib.big.BigRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.big.BigRouter.Configuration.Permanent
import com.badoo.ribs.sandbox.rib.small.builder.SmallBuilder
import kotlinx.android.parcel.Parcelize

class BigRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val smallBuilder: SmallBuilder
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    // TODO consider addressing case like this where there's only permanent parts
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Small : Permanent()
        }
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
            Permanent.Small -> attach { smallBuilder.build(it) }
            Content.Default -> noop()
        }
}
