package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Node.ViewAttachMode
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.DialogLauncher

class DialogRoutingAction<V : RibView, Event : Any>(
    private val router: Router<*, *, *, *, *>,
    private val dialogLauncher: DialogLauncher,
    private val dialog: Dialog<Event>
) : RoutingAction<V> {

    override fun buildNodes(bundles: List<Bundle?>) : List<Node.Descriptor> =
        dialog.buildNodes(bundles).map {
            Node.Descriptor(it, ViewAttachMode.EXTERNAL)
        }

    override fun execute() {
        dialogLauncher.show(dialog, onClose = {
            router.popBackStack()
        })
    }

    override fun cleanup() {
        dialogLauncher.hide(dialog)
    }

    companion object {
        fun <V : RibView> showDialog(
            router: Router<*, *, *, *, *>,
            dialogLauncher: DialogLauncher,
            dialog: Dialog<*>
        ): RoutingAction<V> =
            DialogRoutingAction(router, dialogLauncher, dialog)
    }
}
