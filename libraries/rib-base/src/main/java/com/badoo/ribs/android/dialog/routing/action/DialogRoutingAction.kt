package com.badoo.ribs.android.dialog.routing.action

import android.os.Parcelable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.routing.source.RoutingSource
import com.badoo.ribs.core.routing.Routing
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.DialogLauncher

class DialogRoutingAction<Event : Any, C : Parcelable>(
    private val routingSource: RoutingSource<C>,
    private val routingElementId: Routing.Identifier,
    private val dialogLauncher: DialogLauncher,
    private val dialog: Dialog<Event>
) : RoutingAction {

    override val nbNodesToBuild: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>) : List<Rib> =
        dialog.buildNodes(buildContexts.first())

    override fun execute() {
        dialogLauncher.show(dialog, onClose = {
            routingSource.remove(routingElementId)
        })
    }

    override fun cleanup() {
        dialogLauncher.hide(dialog)
    }

    companion object {
        fun <C : Parcelable> showDialog(
            routingSource: RoutingSource<C>,
            routingElementId: Routing.Identifier,
            dialogLauncher: DialogLauncher,
            dialog: Dialog<*>
        ): RoutingAction =
            DialogRoutingAction(routingSource, routingElementId, dialogLauncher, dialog)
    }
}
