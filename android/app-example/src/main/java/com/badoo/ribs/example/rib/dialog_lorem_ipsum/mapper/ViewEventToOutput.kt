package com.badoo.ribs.example.rib.dialog_lorem_ipsum.mapper

import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsum
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumView

internal object ViewEventToOutput : (DialogLoremIpsumView.Event) -> DialogLoremIpsum.Output? {

    override fun invoke(event: DialogLoremIpsumView.Event): DialogLoremIpsum.Output? = when (event) {
        is DialogLoremIpsumView.Event.ButtonClicked -> DialogLoremIpsum.Output.SomeEvent
    }

}
