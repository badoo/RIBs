package com.badoo.ribs.example.rib.portal_overlay_test

import com.badoo.ribs.core.BuildParams
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.portal_overlay_test.PortalOverlayTestRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class PortalOverlayTestRouter(
    buildParams: BuildParams<Nothing?>
): Router<Configuration, Permanent, Content, Overlay, PortalOverlayTestView>(
    buildParams = buildParams,
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

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<PortalOverlayTestView> =
        RoutingAction.noop()
}
