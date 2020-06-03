package com.badoo.ribs.sandbox.rib.small

import android.os.Parcelable
import com.badoo.ribs.core.routing.router.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.action.AnchoredAttachRoutingAction.Companion.anchor
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.sandbox.rib.big.builder.BigBuilder
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTestBuilder
import com.badoo.ribs.sandbox.rib.small.SmallRouter.Configuration
import com.badoo.ribs.sandbox.rib.small.SmallRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.small.SmallRouter.Configuration.FullScreen
import kotlinx.android.parcel.Parcelize

class SmallRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val bigBuilder: BigBuilder,
    private val portalOverlayTestBuilder: PortalOverlayTestBuilder
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

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
            Content.Default -> noop()
            FullScreen.ShowBig -> anchor(node) { bigBuilder.build(it) }
            FullScreen.ShowOverlay -> anchor(node) { portalOverlayTestBuilder.build(it) }
        }
}
