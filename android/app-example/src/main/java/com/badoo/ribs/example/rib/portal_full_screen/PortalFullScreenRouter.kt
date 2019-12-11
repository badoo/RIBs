package com.badoo.ribs.example.rib.portal_full_screen

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter.Configuration
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter.Configuration.Content
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.portal_full_screen.PortalFullScreenRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.portal_sub_screen.builder.PortalSubScreenBuilder
import kotlinx.android.parcel.Parcelize

class PortalFullScreenRouter(
    savedInstanceState: Bundle?,
    private val smallBuilder: PortalSubScreenBuilder
): Router<Configuration, Permanent, Content, Overlay, PortalFullScreenView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = listOf(Permanent.Small)
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize object Small : Permanent()
        }
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<PortalFullScreenView> =
        when (configuration) {
            Permanent.Small -> attach { smallBuilder.build(it) }
            Content.Default -> noop()
        }
}
