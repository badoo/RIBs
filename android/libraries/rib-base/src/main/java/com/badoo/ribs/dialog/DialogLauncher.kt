package com.badoo.ribs.dialog

interface DialogLauncher {

    fun show(dialog: Dialog<*>)

    fun hide(dialog: Dialog<*>)
}
