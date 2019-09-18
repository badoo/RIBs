package com.badoo.ribs.template.rib_with_view.foo_bar

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter.Configuration
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter.Configuration.Content
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter.Configuration.Overlay
import com.badoo.ribs.template.rib_with_view.foo_bar.FooBarRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class FooBarRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Permanent, Content, Overlay, FooBarView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Content.Default,
    permanentParts = emptyList()
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration()
        sealed class Content : Configuration() {
            @Parcelize object Default : Content()
        }
        sealed class Overlay : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<FooBarView> =
        RoutingAction.noop()
}
