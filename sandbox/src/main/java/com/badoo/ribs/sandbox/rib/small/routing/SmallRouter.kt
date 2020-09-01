package com.badoo.ribs.sandbox.rib.small.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.remoteChild
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.resolution.Resolution.Companion.noop
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter.Configuration
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.small.routing.SmallRouter.Configuration.FullScreen
import kotlinx.android.parcel.Parcelize

class SmallRouter internal constructor(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val builders: SmallChildBuilders
): Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class FullScreen : Configuration() {
            @Parcelize object ShowBig : FullScreen()
            @Parcelize object ShowOverlay : FullScreen()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                Content.Default -> noop()
                FullScreen.ShowBig -> remoteChild(anchor = node) { big.build(it) }
                FullScreen.ShowOverlay -> remoteChild(anchor = node) { portalOverlay.build(it) }
            }
        }

}

