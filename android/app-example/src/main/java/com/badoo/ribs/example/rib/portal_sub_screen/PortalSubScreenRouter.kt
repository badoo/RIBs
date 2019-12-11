package com.badoo.ribs.example.rib.portal_sub_screen

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AnchoredAttachRoutingAction.Companion.anchor
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.example.rib.portal_full_screen.builder.PortalFullScreenBuilder
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayBuilder
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenRouter.Configuration
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_sub_screen.PortalSubScreenRouter.Configuration.FullScreen
import kotlinx.android.parcel.Parcelize

class PortalSubScreenRouter(
    savedInstanceState: Bundle?,
    private val bigBuilder: PortalFullScreenBuilder,
    private val portalOverlayTestBuilder: PortalOverlayBuilder
): Router<Configuration, Nothing, Content, Nothing, PortalSubScreenView>(
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

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<PortalSubScreenView> =
        when (configuration) {
            Content.Default -> noop()
            FullScreen.ShowBig -> anchor(node) { bigBuilder.build(it) }
            FullScreen.ShowOverlay -> anchor(node) { portalOverlayTestBuilder.build(it) }
        }
}
