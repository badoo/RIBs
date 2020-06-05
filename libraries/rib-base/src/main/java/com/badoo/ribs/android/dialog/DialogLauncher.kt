package com.badoo.ribs.android.dialog

interface DialogLauncher {

    fun show(dialog: Dialog<*>, onClose: () -> Unit)

    fun hide(dialog: Dialog<*>)
}
