package com.badoo.ribs.test.integrationpoint

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.test.assertInstanceOf
import com.badoo.ribs.test.assertTrue
import kotlin.reflect.KClass

class TestRibDialogLauncher : DialogLauncher {

    private var currentDialog: Dialog<*>? = null

    override fun hide(dialog: Dialog<*>) {
        //no-op
        currentDialog = dialog
    }

    override fun show(dialog: Dialog<*>, onClose: () -> Unit) {
        currentDialog = dialog
    }

    inline fun <reified T : Dialog<*>> verifyDialogShown() {
        verifyDialogShown(T::class)
    }

    fun <T : Dialog<*>> verifyDialogShown(dialog: KClass<T>) {
        assertInstanceOf(dialog, currentDialog)
    }

    fun verifyDialogNotShown() {
        assertTrue(currentDialog == null) { "$currentDialog has been shown" }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> sendEvent(event: T) {
        (currentDialog as? Dialog<T>)?.publish(event)
    }

}
