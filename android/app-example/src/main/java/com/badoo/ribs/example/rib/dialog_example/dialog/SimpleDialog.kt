package com.badoo.ribs.example.rib.dialog_example.dialog

import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.dialog.Dialog.Event.Negative
import com.badoo.ribs.dialog.Dialog.Event.Neutral
import com.badoo.ribs.dialog.Dialog.Event.Positive


class SimpleDialog : Dialog<Dialog.Event>({
    // Inject (Smart)Resources in constructor to resolve texts if needed
    title = "Simple dialog"
    message = "Lorem ipsum dolor sit amet"
    buttons {
        positive("Yay!", Positive)
        negative("No way", Negative)
        neutral("Meh?", Neutral)
    }

    cancellationPolicy = Cancellable(
        event = Event.Cancelled,
        cancelOnTouchOutside = false
    )
})
