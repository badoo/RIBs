package com.badoo.ribs.example.rib.small

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AnchoredAttachRoutingAction.Companion.anchor
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.example.rib.big.builder.BigBuilder
import com.badoo.ribs.example.rib.portal_overlay_test.builder.PortalOverlayTestBuilder
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration.Content
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration.FullScreen
import kotlinx.android.parcel.Parcelize

class SmallRouter(
    savedInstanceState: Bundle?,
    private val bigBuilder: BigBuilder,
    private val portalOverlayTestBuilder: PortalOverlayTestBuilder
): Router<Configuration, Nothing, Content, Nothing, SmallView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
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

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<SmallView> =
        when (configuration) {
            Content.Default -> noop()
            FullScreen.ShowBig -> anchor(node) { bigBuilder.build(it) }
            FullScreen.ShowOverlay -> anchor(node) { portalOverlayTestBuilder.build(it) }
        }
}
