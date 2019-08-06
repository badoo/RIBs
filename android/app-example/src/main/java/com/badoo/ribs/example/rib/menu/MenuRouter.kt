package com.badoo.ribs.example.rib.menu

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.menu.MenuRouter.Configuration
import kotlinx.android.parcel.Parcelize
import android.os.Bundle

class MenuRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Nothing, Configuration, Nothing, MenuView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<MenuView> =
        RoutingAction.noop()
}
