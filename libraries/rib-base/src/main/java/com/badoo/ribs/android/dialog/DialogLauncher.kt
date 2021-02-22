package com.badoo.ribs.android.dialog

import androidx.appcompat.app.AlertDialog

interface DialogLauncher {

    fun show(dialog: Dialog<*>, onClose: () -> Unit, onShown: (AlertDialog) -> Unit)

    fun hide(dialog: Dialog<*>)
}
