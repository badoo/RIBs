package com.badoo.ribs.example.rib.menu

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.menu.MenuRouter.Configuration
import kotlinx.android.parcel.Parcelize
import com.badoo.ribs.core.builder.BuildParams

class MenuRouter(
    buildParams: BuildParams<Nothing?>
): Router<Configuration, Nothing, Configuration, Nothing, MenuView>(
    buildParams = buildParams,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<MenuView> =
        RoutingAction.noop()
}
