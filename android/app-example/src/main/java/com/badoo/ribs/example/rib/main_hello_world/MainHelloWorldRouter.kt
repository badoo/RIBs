package com.badoo.ribs.example.rib.main_hello_world

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldRouter.Configuration
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldRouter.Configuration.Content
import com.badoo.ribs.example.rib.main_hello_world.MainHelloWorldRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.portal_sub_screen.builder.PortalSubScreenBuilder
import kotlinx.android.parcel.Parcelize

class MainHelloWorldRouter(
    savedInstanceState: Bundle?,
    private val smallBuilder: PortalSubScreenBuilder
): Router<Configuration, Permanent, Content, Nothing, MainHelloWorldView>(
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
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<MainHelloWorldView> =
        when (configuration) {
            Permanent.Small -> attach { smallBuilder.build(it) }
            Content.Default -> noop()
    }
}
