package com.badoo.ribs.sandbox.rib.dialog_example.dialog

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleChildBuilders

class Dialogs internal constructor(
    builders: DialogExampleChildBuilders
) {
    val themedDialog: Dialog<Dialog.Event> = ThemedDialog()
    val simpleDialog: Dialog<Dialog.Event> = SimpleDialog()
    val lazyDialog: Dialog<Dialog.Event> = LazyDialog()
    val ribDialog: Dialog<Dialog.Event> = RibDialog(
        builders.loremIpsum
    )
}
