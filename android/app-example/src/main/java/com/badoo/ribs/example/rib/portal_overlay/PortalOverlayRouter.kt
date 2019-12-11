package com.badoo.ribs.example.rib.portal_overlay

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayRouter.Configuration
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.portal_overlay.PortalOverlayRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class PortalOverlayRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Permanent, Content, Overlay, PortalOverlayView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration()
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<PortalOverlayView> =
        RoutingAction.noop()
}
