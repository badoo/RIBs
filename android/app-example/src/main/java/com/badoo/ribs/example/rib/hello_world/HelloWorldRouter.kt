package com.badoo.ribs.example.rib.hello_world

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.DialogRoutingAction.Companion.dialog
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.action.RoutingAction.Companion.noop
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.example.rib.hello_world.HelloWorldRouter.Configuration
import com.badoo.ribs.example.rib.hello_world.dialog.SimpleDialog
import com.badoo.ribs.example.rib.hello_world.dialog.SomeRibDialog
import kotlinx.android.parcel.Parcelize

class HelloWorldRouter(
    private val dialogLauncher: DialogLauncher,
    private val simpleDialog: SimpleDialog,
    private val someRibDialog: SomeRibDialog
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
            Configuration.AskOpinion -> dialog(dialogLauncher, simpleDialog)
//            Configuration.AskOpinion -> dialog(dialogLauncher, someRibDialog)
    }
}
