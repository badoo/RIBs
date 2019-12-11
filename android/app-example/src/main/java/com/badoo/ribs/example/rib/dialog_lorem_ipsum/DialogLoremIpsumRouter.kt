package com.badoo.ribs.example.rib.dialog_lorem_ipsum

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumRouter.Configuration
import kotlinx.android.parcel.Parcelize

class DialogLoremIpsumRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Nothing, Configuration, Nothing, DialogLoremIpsumView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<DialogLoremIpsumView> =
        RoutingAction.noop()
}
