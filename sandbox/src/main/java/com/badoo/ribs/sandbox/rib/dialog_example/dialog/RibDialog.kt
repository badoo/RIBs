package com.badoo.ribs.sandbox.rib.dialog_example.dialog

import com.badoo.ribs.android.text.Text
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.Dialog.Event.Negative
import com.badoo.ribs.android.dialog.Dialog.Event.Positive
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsumBuilder

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
