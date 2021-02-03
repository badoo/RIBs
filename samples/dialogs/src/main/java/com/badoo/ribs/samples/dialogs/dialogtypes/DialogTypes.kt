package com.badoo.ribs.samples.dialogs.dialogtypes

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.samples.dialogs.dialogtypes.LazyDialog
import com.badoo.ribs.samples.dialogs.dialogtypes.RibDialog
import com.badoo.ribs.samples.dialogs.dialogtypes.SimpleDialog
import com.badoo.ribs.samples.dialogs.dialogtypes.ThemedDialog

class DialogTypes {
    val themedDialog: Dialog<Dialog.Event> = ThemedDialog()
    val simpleDialog: Dialog<Dialog.Event> = SimpleDialog()
    val lazyDialog: Dialog<Dialog.Event> = LazyDialog()
    val ribDialog: Dialog<Dialog.Event> = RibDialog()
}