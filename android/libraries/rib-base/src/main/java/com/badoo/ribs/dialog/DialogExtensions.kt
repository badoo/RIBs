package com.badoo.ribs.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import android.widget.FrameLayout
import com.badoo.ribs.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.dialog.Dialog.CancellationPolicy.NonCancellable

fun <Event : Any> Dialog<Event>.toAlertDialog(context: Context, onClose: () -> Unit) : AlertDialog =
    AlertDialog.Builder(context)
        .apply {
            setCancelable(this@toAlertDialog, onClose)
            setRib(this@toAlertDialog, context)
            setTexts(this@toAlertDialog)
            setButtons(this@toAlertDialog)
        }
        .create()
        .apply {
            setCanceledOnTouchOutside(this@toAlertDialog)
            setButtonClickListeners(this@toAlertDialog, onClose)
        }

private fun <Event : Any> AlertDialog.Builder.setCancelable(dialog: Dialog<Event>, onClose: () -> Unit) {
    when (val policy = dialog.cancellationPolicy) {
        is NonCancellable -> {
            setCancelable(false)
        }
        is Cancellable -> {
            setCancelable(true)
            setOnCancelListener {
                dialog.publish(policy.event)
                onClose()
            }
        }
    }
}

private fun AlertDialog.Builder.setRib(dialog: Dialog<*>, context: Context) {
    dialog.rib?.let {
        setView(object : FrameLayout(context) {
            override fun onAttachedToWindow() {
                super.onAttachedToWindow()
                it.attachToView(this)
            }

            override fun onDetachedFromWindow() {
                super.onDetachedFromWindow()
                it.detachFromView()
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

private fun <Event : Any> AlertDialog.setButtonClickListeners(dialog: Dialog<Event>, onClose: () -> Unit) {
    // Workaround so that pressing button will not close dialog automatically. Let business
    // logic decide what to do instead.
    setOnShowListener {
        (it as AlertDialog).apply {
            configureButtonClick(AlertDialog.BUTTON_POSITIVE, dialog, dialog.buttons?.positive, onClose)
            configureButtonClick(AlertDialog.BUTTON_NEGATIVE, dialog, dialog.buttons?.negative, onClose)
            configureButtonClick(AlertDialog.BUTTON_NEUTRAL, dialog, dialog.buttons?.neutral, onClose)
        }
    }
}

private fun <Event : Any> AlertDialog.configureButtonClick(
    button: Int,
    dialog: Dialog<Event>,
    buttonConfig: Dialog.ButtonsConfig.ButtonConfig<Event>?,
    onClose: () -> Unit
) {
    getButton(button).setOnClickListener {
        buttonConfig?.run {
            onClickEvent?.let { dialog.publish(it) }
            if (closesDialogAutomatically) {
                onClose()
            }
        }
    }
}

private fun <Event : Any> AlertDialog.setCanceledOnTouchOutside(dialog: Dialog<Event>) {
    setCanceledOnTouchOutside(
        when (val policy = dialog.cancellationPolicy) {
            is NonCancellable -> false
            is Cancellable -> policy.cancelOnTouchOutside
        }
    )
}

