package com.badoo.ribs.sandbox.rib.dialog_example.dialog

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.android.dialog.Dialog.Event.Negative
import com.badoo.ribs.android.dialog.Dialog.Event.Neutral
import com.badoo.ribs.android.dialog.Dialog.Event.Positive
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.sandbox.R

class ThemedDialog : Dialog<Dialog.Event>({
    // Inject (Smart)Resources in constructor to resolve texts if needed
    title = Text.Resource(R.string.show_themed_dialog)
    message = Text.Plain("Lorem ipsum dolor sit amet")
    buttons {
        positive(Text.Plain("Yay!"), Positive)
        negative(Text.Plain("No way"), Negative)
        neutral(Text.Plain("Meh?"), Neutral)
    }
    themeResId = R.style.AppTheme_ThemedDialog

    cancellationPolicy = Cancellable(
        event = Event.Cancelled,
        cancelOnTouchOutside = false
    )
})
