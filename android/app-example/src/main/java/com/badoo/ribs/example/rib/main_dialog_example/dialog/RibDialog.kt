package com.badoo.ribs.example.rib.main_dialog_example.dialog

import com.badoo.ribs.android.Text
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.Event.Negative
import com.badoo.ribs.dialog.Dialog.Event.Positive
import com.badoo.ribs.example.rib.dialog_lorem_ipsum.DialogLoremIpsumBuilder

class RibDialog(
    loremIpsumBuilder: DialogLoremIpsumBuilder
) : Dialog<Dialog.Event>({
        title = Text.Plain("A title if you wish")
        ribFactory {
            loremIpsumBuilder.build(it)
        }
        buttons {
            positive(Text.Plain("Ok"), Positive)
            negative(Text.Plain("Cancel"), Negative)
        }
    }
)
