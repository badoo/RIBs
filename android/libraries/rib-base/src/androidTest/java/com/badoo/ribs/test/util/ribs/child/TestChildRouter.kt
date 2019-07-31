package com.badoo.ribs.test.util.ribs.child

import android.os.Parcelable
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.test.util.ribs.child.TestChildRouter.Configuration
import kotlinx.android.parcel.Parcelize
import android.os.Bundle

class TestChildRouter(
    savedInstanceState: Bundle?
): Router<Configuration, Nothing, Configuration, Nothing, TestChildView>(
    savedInstanceState = savedInstanceState,
    initialConfiguration = Configuration.Default
) {
    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<TestChildView> =
        RoutingAction.noop()
}
