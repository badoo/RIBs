package com.badoo.ribs.tutorials.tutorial1.rib.hello_world

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.tutorials.tutorial1.rib.hello_world.HelloWorldRouter.Configuration.Content
import kotlinx.android.parcel.Parcelize

class HelloWorldRouter: Router<Configuration, Nothing, Content, Nothing, HelloWorldView>(
    initialConfiguration = Content.Default
) {
    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<HelloWorldView> =
        RoutingAction.noop()
}
