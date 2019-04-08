package com.badoo.ribs.test.util.ribs.root

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import kotlinx.android.parcel.Parcelize

class TestRootRouter(
    private val builder1: () -> Node<*>,
    private val builder2: () -> Node<*>
): Router<Configuration, TestRootView>(
    initialConfiguration = Configuration.Default
) {
    override val permanentParts: List<() -> Node<*>> =
        emptyList()

    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
        @Parcelize object Node1 : Configuration()
        @Parcelize object Node2 : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<TestRootView> =
        when (configuration) {
            Configuration.Default -> noop()
            Configuration.Node1 -> attach(builder1)
            Configuration.Node2 -> attach(builder2)
        }
}
