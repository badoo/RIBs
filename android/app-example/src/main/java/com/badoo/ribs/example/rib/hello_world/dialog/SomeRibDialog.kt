package com.badoo.ribs.example.rib.hello_world.dialog

import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.Event.Negative
import com.badoo.ribs.dialog.Dialog.Event.Positive
import com.badoo.ribs.example.rib.lorem_ipsum.builder.LoremIpsumBuilder

class SomeRibDialog(
    loremIpsumBuilder: LoremIpsumBuilder
) : Dialog<Dialog.Event>({
        title = "A title if you wish"
        ribFactory {
            loremIpsumBuilder.build()
        }
        buttons {
            positive("Ok", Positive)
            negative("Cancel", Negative)
        }
    }
)
