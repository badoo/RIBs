package com.badoo.ribs.android.dialog

import android.content.Context
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.badoo.ribs.android.AndroidRibViewHost
import com.badoo.ribs.android.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.android.dialog.Dialog.CancellationPolicy.NonCancellable

fun <Event : Any> Dialog<Event>.toAlertDialog(context: Context, onClose: () -> Unit): AlertDialog {
    val builder = themeResId?.let { AlertDialog.Builder(context, it) }
            ?: AlertDialog.Builder(context)

    return builder
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
                setOnDismissListener { this@toAlertDialog.rib = null }
            }
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
            val host = AndroidRibViewHost(this)

            init {
                // If we will execute multiple configurations in a short period of time ->
                // Node can be attached to a host multiple times ->
                // java.lang.IllegalStateException: The specified child already has a parent.
                // Detach node here -> no harmful effect when view is not attached
                // PushTwoPopOneDefaultTest.noPermanent_singleInitial_pushOverlay_pushContent_pop - to reproduce
                host.detachChild(it.node)

                it.node.onCreateView(host)
                host.attachChild(it.node)
            }

            override fun onDetachedFromWindow() {
                super.onDetachedFromWindow()
                host.detachChild(it.node)
            }
        })
    }
}

private fun AlertDialog.Builder.setTexts(dialog: Dialog<*>) {
    dialog.title?.let { setTitle(it.resolve(context)) }
    dialog.message?.let { setMessage(it.resolve(context)) }
}

private fun AlertDialog.Builder.setButtons(dialog: Dialog<*>) {
    dialog.buttons?.positive?.let { config ->
        // Pass null for listener, and implement it below in OnShowListener to prevent
        // Android default behavior of closing the dialog automatically.
        // Dialogs are Router configuration based, so the corresponding configurations needs
        // to change and that as a result should close the dialog - not the dialog automatically
        // by itself, because that would leave the Router stuck in the dialog configuration.
        setPositiveButton(config.title?.resolve(context), null)
    }
    dialog.buttons?.negative?.let { config ->
        setNegativeButton(config.title?.resolve(context), null)
    }
    dialog.buttons?.neutral?.let { config ->
        setNeutralButton(config.title?.resolve(context), null)
    }
}

private fun <Event : Any> AlertDialog.setButtonClickListeners(dialog: Dialog<Event>, onClose: () -> Unit) {
    // Workaround so that pressing button will not close dialog automatically. Let business
    // logic decide what to do instead.
    setOnShowListener {
        (it as? AlertDialog)?.apply {
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

