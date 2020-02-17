package com.badoo.ribs.example.rib.switcher.dialog

import com.badoo.ribs.android.Text
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.dialog.Dialog.Event.Positive


/**
 * This dialog should be tested where there are other Configurations as well,
 * demonstrating that they do not get detached while this dialog is on the screen.
 */
class DialogToTestOverlay(
    private val builder: ((BuildContext) -> Node<*>)? = null
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

    builder?.let {
        ribFactory(it)
    }
})
