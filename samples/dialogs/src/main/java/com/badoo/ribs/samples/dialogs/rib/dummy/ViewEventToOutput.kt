package com.badoo.ribs.samples.dialogs.rib.dummy

import com.badoo.ribs.samples.dialogs.rib.dummy.Dummy.Output
import com.badoo.ribs.samples.dialogs.rib.dummy.DummyView.Event

internal object ViewEventToOutput : (Event) -> Output? {

    override fun invoke(event: Event): Output? = when (event) {
        is Event.ButtonClicked -> Output.SomeEvent
    }
}
