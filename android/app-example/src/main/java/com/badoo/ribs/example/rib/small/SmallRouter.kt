package com.badoo.ribs.example.rib.small

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Portal
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.FullScreenRoutingAction.Companion.fullScreen
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.example.rib.big.builder.BigBuilder
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration
import com.badoo.ribs.example.rib.small.SmallRouter.Configuration.Content
import kotlinx.android.parcel.Parcelize

class SmallRouter(
    savedInstanceState: Bundle?,
//    private val portal: Portal.Sink,
    private val bigBuilder: BigBuilder
): Router<Configuration, Nothing, Content, Nothing, SmallView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
            @Parcelize object ShowBig : Content()
        }
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<SmallView> =
        when (configuration) {
            Content.Default -> noop()
            Content.ShowBig -> attach { bigBuilder.build(it) }
//            Content.ShowBig -> portal.remoteRoutingAction { bigBuilder.build(it) } as RoutingAction<SmallView>
        }
}
