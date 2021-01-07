package com.badoo.ribs.android.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.util.WeakHashMap

class DialogLauncherImpl(
    private val context: Context
) : DialogLauncher {

    private val dialogs: WeakHashMap<Dialog<*>, AlertDialog> = WeakHashMap()

    override fun show(dialog: Dialog<*>, onClose: () -> Unit) {
        dialogs[dialog] = dialog.toAlertDialog(context, onClose).also {
            it.show()
        }
    }

    override fun hide(dialog: Dialog<*>) {
        dialogs[dialog]?.dismiss()
    }

    fun hideAll() {
        dialogs.values.forEach { it.dismiss() }
    }

}
