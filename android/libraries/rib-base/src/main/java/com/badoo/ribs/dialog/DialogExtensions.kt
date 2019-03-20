package com.badoo.ribs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.FrameLayout

fun <Event : Any> Dialog<Event>.toAlertDialog(context: Context) : AlertDialog =
    AlertDialog.Builder(context)
        .apply {
            setRib(this@toAlertDialog, context)
            setTexts(this@toAlertDialog)
            setButtons(this@toAlertDialog)
        }
        .create()
        .apply {
            setButtonClickListeners(this@toAlertDialog)
        }

private fun AlertDialog.Builder.setRib(
    dialog: Dialog<*>,
    context: Context
) {
    dialog.rib?.let {
        setView(object : FrameLayout(context) {
            override fun onAttachedToWindow() {
                super.onAttachedToWindow()
                it.attachToView(this)
            }

            override fun onDetachedFromWindow() {
                super.onDetachedFromWindow()
                it.detachFromView(this)
            }
        })
    }
}

private fun AlertDialog.Builder.setTexts(dialog: Dialog<*>) {
    dialog.title?.let { setTitle(it) }
    dialog.message?.let { setMessage(it) }
}

private fun AlertDialog.Builder.setButtons(dialog: Dialog<*>) {
    dialog.buttons?.positive?.let { config ->
        // Pass null for listener, and implement it below in OnShowListener to prevent
        // Android default behavior of closing the dialog automatically.
        // Dialogs are Router configuration based, so the corresponding configurations needs
        // to change and that as a result should close the dialog - not the dialog automatically
        // by itself, because that would leave the Router stuck in the dialog configuration.
        setPositiveButton(config.title, null)
    }
    dialog.buttons?.negative?.let { config ->
        setNegativeButton(config.title, null)
    }
    dialog.buttons?.neutral?.let { config ->
        setNeutralButton(config.title, null)
    }
}

private fun <Event : Any> AlertDialog.setButtonClickListeners(dialog: Dialog<Event>) {
    // Workaround so that pressing button will not close dialog automatically. Let business
    // logic decide what to do instead.
    setOnShowListener {
        (it as AlertDialog).apply {
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                dialog.buttons?.positive?.onClickEvent?.let { dialog.publish(it) }
            }
            getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                dialog.buttons?.negative?.onClickEvent?.let { dialog.publish(it) }
            }
            getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                dialog.buttons?.neutral?.onClickEvent?.let { dialog.publish(it) }
            }
        }
    }
}
