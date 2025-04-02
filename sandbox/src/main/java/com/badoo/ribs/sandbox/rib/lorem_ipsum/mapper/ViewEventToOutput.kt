package com.badoo.ribs.sandbox.rib.lorem_ipsum.mapper

import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsumView

internal object ViewEventToOutput : (LoremIpsumView.Event) -> LoremIpsum.Output {

    override fun invoke(event: LoremIpsumView.Event): LoremIpsum.Output = when (event) {
        is LoremIpsumView.Event.ButtonClicked -> LoremIpsum.Output.SomeEvent
    }

}
