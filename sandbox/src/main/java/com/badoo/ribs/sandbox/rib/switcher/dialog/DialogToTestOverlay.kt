package com.badoo.ribs.sandbox.rib.switcher.dialog

import com.badoo.ribs.android.text.Text
import com.badoo.ribs.routing.resolution.RibFactory
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.android.dialog.Dialog.Event.Positive


/**
 * This dialog should be tested where there are other Configurations as well,
 * demonstrating that they do not get detached while this dialog is on the screen.
 */
class DialogToTestOverlay(
    private val ribFactory: RibFactory? = null
) : Dialog<Dialog.Event>({
    title = Text.Plain("Test overlay")
    message = Text.Plain("Watch the background behind this dialog, is it still there?")
    buttons {
        positive(Text.Plain("Ok man whatever"), Positive)
    }

    cancellationPolicy = Cancellable(
        event = Event.Cancelled,
        cancelOnTouchOutside = false
    )

    ribFactory?.let {
        ribFactory(it)
    }
})
