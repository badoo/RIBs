package com.badoo.ribs.example.rib.hello_world.dialog

import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.Event.Negative
import com.badoo.ribs.dialog.Dialog.Event.Neutral
import com.badoo.ribs.dialog.Dialog.Event.Positive


class SimpleDialog : Dialog<Dialog.Event>({
    // Inject (Smart)Resources in constructor to resolve texts if needed
    title = "Some dialog"
    message = "Lorem ipsum dolor sit amet"
    buttons {
        positive("Yay!", Positive)
        negative("No way", Negative)
        neutral("Meh?", Neutral)
    }
})
