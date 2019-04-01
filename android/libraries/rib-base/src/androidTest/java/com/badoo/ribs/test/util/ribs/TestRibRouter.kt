package com.badoo.ribs.test.util.ribs

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.test.util.ribs.TestRibRouter.Configuration
import kotlinx.android.parcel.Parcelize

class TestRibRouter: Router<Configuration, TestRibView>(
    initialConfiguration = Configuration.Default
) {
    override val permanentParts: List<() -> Node<*>> =
        emptyList()

    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<TestRibView> =
        RoutingAction.noop()
}
