package com.badoo.ribs.test.util

import androidx.appcompat.app.AlertDialog
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.DialogLauncher

class NoOpDialogLauncher : DialogLauncher {

    override fun show(dialog: Dialog<*>, onClose: () -> Unit, onShown: (AlertDialog) -> Unit) {
    }

    override fun hide(dialog: Dialog<*>) {
    }
}
