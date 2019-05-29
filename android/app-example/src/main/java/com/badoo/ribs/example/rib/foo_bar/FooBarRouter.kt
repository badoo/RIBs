package com.badoo.ribs.example.rib.foo_bar

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.foo_bar.FooBarRouter.Configuration
import kotlinx.android.parcel.Parcelize

class FooBarRouter: Router<Configuration, Nothing, Configuration, Nothing, FooBarView>(
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<FooBarView> =
        RoutingAction.noop()
}
