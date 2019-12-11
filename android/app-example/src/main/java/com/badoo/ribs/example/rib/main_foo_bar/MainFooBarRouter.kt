package com.badoo.ribs.example.rib.main_foo_bar

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.main_foo_bar.MainFooBarRouter.Configuration
import kotlinx.android.parcel.Parcelize

class MainFooBarRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Nothing, Configuration, Nothing, MainFooBarView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<MainFooBarView> =
        RoutingAction.noop()
}
