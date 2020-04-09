package com.badoo.ribs.example.rib.dialog_example.dialog

import com.badoo.ribs.android.Text
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.Event.Negative
import com.badoo.ribs.dialog.Dialog.Event.Positive
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumBuilder

class RibDialog(
    loremIpsumBuilder: LoremIpsumBuilder
) : Dialog<Dialog.Event>({
        title = Text.Plain("A title if you wish")
        nodeFactory {
            loremIpsumBuilder.build(it)
        }
        buttons {
            positive(Text.Plain("Ok"), Positive)
            negative(Text.Plain("Cancel"), Negative)
        }
    }
)
