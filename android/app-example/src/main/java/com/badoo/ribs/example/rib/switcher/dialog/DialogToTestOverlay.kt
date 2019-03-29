package com.badoo.ribs.example.rib.switcher.dialog

import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.dialog.Dialog.Event.Positive


/**
 * This dialog should be tested where there are other Configurations as well,
 * demonstrating that they do not get detached while this dialog is on the screen.
 */
class DialogToTestOverlay : Dialog<Dialog.Event>({
    title = "Test overlay"
    message = "Watch the background behind this dialog, is it still there?"
    buttons {
        positive("Ok man whatever", Positive)
    }

    cancellationPolicy = Cancellable(
        event = Event.Cancelled,
        cancelOnTouchOutside = false
    )
})
