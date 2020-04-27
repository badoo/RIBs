package com.badoo.ribs.core.routing.action

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.DialogLauncher

class DialogRoutingAction<Event : Any>(
    private val router: Router<*, *, *, *, *>,
    private val dialogLauncher: DialogLauncher,
    private val dialog: Dialog<Event>
) : RoutingAction {

    override fun buildNodes(ancestryInfo: AncestryInfo, bundles: List<Bundle?>) : List<Node<*>> =
        dialog.buildNodes(ancestryInfo, bundles)

    override fun execute() {
        dialogLauncher.show(dialog, onClose = {
            router.popBackStack()
        })
    }

    override fun cleanup() {
        dialogLauncher.hide(dialog)
    }

    companion object {
        fun showDialog(
            router: Router<*, *, *, *, *>,
            dialogLauncher: DialogLauncher,
            dialog: Dialog<*>
        ): RoutingAction =
            DialogRoutingAction(router, dialogLauncher, dialog)
    }
}
