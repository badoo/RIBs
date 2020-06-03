package com.badoo.ribs.dialog

interface DialogLauncher {

    fun show(dialog: Dialog<*>, onClose: () -> Unit)

    fun hide(dialog: Dialog<*>)
}
