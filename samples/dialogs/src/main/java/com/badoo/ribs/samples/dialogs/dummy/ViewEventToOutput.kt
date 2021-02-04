package com.badoo.ribs.samples.dialogs.dummy

import com.badoo.ribs.samples.dialogs.dummy.Dummy.Output
import com.badoo.ribs.samples.dialogs.dummy.LoremIpsumView.Event

internal object ViewEventToOutput : (Event) -> Output? {

    override fun invoke(event: Event): Output? = when (event) {
        is Event.ButtonClicked -> Output.SomeEvent
    }
}
