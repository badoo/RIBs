package com.badoo.ribs.samples.android.dialogs.dialogs

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.samples.android.dialogs.rib.dummy.DummyBuilder

class Dialogs {
    val themedDialog: Dialog<Dialog.Event> = ThemedDialog()
    val simpleDialog: Dialog<Dialog.Event> = SimpleDialog()
    val lazyDialog: Dialog<Dialog.Event> = LazyDialog()
    val ribDialog: Dialog<Dialog.Event> = RibDialog(DummyBuilder())
}
