package com.badoo.ribs.sandbox.rib.dialog_example.dialog

import com.badoo.ribs.android.text.Text
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.android.dialog.Dialog.Event.Negative
import com.badoo.ribs.android.dialog.Dialog.Event.Neutral
import com.badoo.ribs.android.dialog.Dialog.Event.Positive
import com.badoo.ribs.sandbox.R


class ThemeDialog : Dialog<Dialog.Event>({
    // Inject (Smart)Resources in constructor to resolve texts if needed
    title = Text.Plain("Theme dialog")
    message = Text.Plain("Lorem ipsum dolor sit amet")
    buttons {
        positive(Text.Plain("Yay!"), Positive)
        negative(Text.Plain("No way"), Negative)
        neutral(Text.Plain("Meh?"), Neutral)
    }
    themeResId = R.style.AppTheme_ThemeDialog

    cancellationPolicy = Cancellable(
        event = Event.Cancelled,
        cancelOnTouchOutside = false
    )
})
