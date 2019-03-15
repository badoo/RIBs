package com.badoo.ribs.dialog

import android.content.Context
import android.support.v7.app.AlertDialog

fun <T : Any> Dialog<T>.toAlertDialog(context: Context) : AlertDialog =
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .also { builder ->
            buttons?.positive?.let { config ->
                // Pass null for listener, and implement it below in OnShowListener to prevent
                // Android default behavior of closing the dialog automatically.
                // Dialogs are Router configuration based, so the corresponding configurations needs
                // to change and that as a result should close the dialog - not the dialog automatically
                // by itself, because that would leave the Router stuck in the dialog configuration.
                builder.setPositiveButton(config.title, null)
            }
            buttons?.negative?.let { config ->
                builder.setNegativeButton(config.title, null)
            }
            buttons?.neutral?.let { config ->
                builder.setNeutralButton(config.title, null)
            }
        }
        .create()
        .apply {
            // Workaround so that pressing button will not close dialog automatically. Let business
            // logic decide what to do instead.
            setOnShowListener {
                (it as AlertDialog).apply {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        buttons?.positive?.onClickEvent?.let { publish(it) }
                    }
                    getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                        buttons?.negative?.onClickEvent?.let { publish(it) }
                    }
                    getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                        buttons?.neutral?.onClickEvent?.let { publish(it) }
                    }
                }
            }
        }
