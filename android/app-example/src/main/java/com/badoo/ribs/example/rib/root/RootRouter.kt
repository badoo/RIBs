package com.badoo.ribs.example.rib.root

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.example.rib.root.RootRouter.Configuration
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Content
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.switcher.builder.SwitcherBuilder
import kotlinx.android.parcel.Parcelize

class RootRouter(
    savedInstanceState: Bundle?,
    private val switcherBuilder: SwitcherBuilder
): Router<Configuration, Permanent, Content, Overlay, RootView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration()
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize object Portal : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<RootView> =
        when (configuration) {
            Content.Default -> AttachRibRoutingAction.attach { switcherBuilder.build(it) }
            Content.Portal -> noop()
        }
}
