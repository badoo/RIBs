package com.badoo.ribs.test.util

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.DialogLauncher

class NoOpDialogLauncher : DialogLauncher {
    
    override fun show(dialog: Dialog<*>, onClose: () -> Unit) {
    }

    override fun hide(dialog: Dialog<*>) {
    }
}
