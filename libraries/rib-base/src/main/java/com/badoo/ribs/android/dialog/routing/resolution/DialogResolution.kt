package com.badoo.ribs.android.dialog.routing.resolution

import android.os.Parcelable
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.source.RoutingSource

class DialogResolution<Event : Any, C : Parcelable>(
    private val routingSource: RoutingSource<C>,
    private val routingElementId: Routing.Identifier,
    private val dialogLauncher: DialogLauncher,
    private val dialog: Dialog<Event>
) : Resolution {

    override val numberOfNodes: Int = 1

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
        ): Resolution =
            DialogResolution(routingSource, routingElementId, dialogLauncher, dialog)
    }
}
