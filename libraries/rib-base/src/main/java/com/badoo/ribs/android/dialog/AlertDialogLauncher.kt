package com.badoo.ribs.android.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import java.util.WeakHashMap

class AlertDialogLauncher(
    private val context: Context,
    lifecycle: Lifecycle
) : DialogLauncher {

    private val dialogs: MutableMap<Dialog<*>, AlertDialog> = WeakHashMap()

    init {
        lifecycle.subscribe(onDestroy = {
            dialogs.values.forEach { it.dismiss() }
        })
    }

    override fun show(dialog: Dialog<*>, onClose: () -> Unit, onShown: (AlertDialog) -> Unit) {
        dialogs[dialog] = dialog.toAlertDialog(context, onClose).also {
            it.show()
            onShown.invoke(it)
        }
    }

    override fun hide(dialog: Dialog<*>) {
        dialogs[dialog]?.dismiss()
    }

}
