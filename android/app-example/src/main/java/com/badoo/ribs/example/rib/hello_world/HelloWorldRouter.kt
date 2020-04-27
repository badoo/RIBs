package com.badoo.ribs.example.rib.hello_world

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter.Configuration.Content
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter.Configuration.Permanent
import com.badoo.ribs.example.rib.small.builder.SmallBuilder
import kotlinx.android.parcel.Parcelize

class HelloWorldRouter(
    buildParams: BuildParams<Nothing?>,
    private val smallBuilder: SmallBuilder
): Router<Configuration, Permanent, Content, Nothing, HelloWorldView>(
    buildParams = buildParams,
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

    override fun resolveConfiguration(configuration: Configuration): RoutingAction =
        when (configuration) {
            Permanent.Small -> attach { smallBuilder.build(it) }
            Content.Default -> noop()
    }
}
