package com.badoo.ribs.example.rib.hello_world

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.CompositeRoutingAction.Companion.composite
import com.badoo.ribs.core.routing.action.InvokeOnCleanup.Companion.cleanup
import com.badoo.ribs.core.routing.action.InvokeOnExecute.Companion.execute
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.example.rib.hello_world.dialog.SimpleDialog
import kotlinx.android.parcel.Parcelize

class HelloWorldRouter(
    private val dialog: SimpleDialog,
    private val dialogLauncher: DialogLauncher
): Router<Configuration, HelloWorldView>(
    initialConfiguration = Configuration.Default
) {
    override val permanentParts: List<() -> Node<*>> =
        emptyList()

    sealed class Configuration : Parcelable {
        @Parcelize object Default : Configuration()
        @Parcelize object AskOpinion : Configuration()
    }

    override fun resolveConfiguration(configuration: Configuration): RoutingAction<HelloWorldView> =
        when (configuration) {
            Configuration.Default -> noop()
            Configuration.AskOpinion -> composite(
                execute { dialogLauncher.show(dialog) },
                cleanup { dialogLauncher.hide(dialog) }
            )
        }
}
