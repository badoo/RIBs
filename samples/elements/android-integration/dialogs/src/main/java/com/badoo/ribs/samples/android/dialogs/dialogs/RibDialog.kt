package com.badoo.ribs.samples.android.dialogs.dialogs

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.samples.android.dialogs.R
import com.badoo.ribs.samples.android.dialogs.rib.dummy.DummyBuilder

class RibDialog(dummyBuilder: DummyBuilder) : Dialog<Dialog.Event>(
    {
        title = Text.Resource(R.string.rib_dialog_title)
        ribFactory {
            dummyBuilder.build(it)
        }
        buttons {
            positive(Text.Resource(R.string.dialog_positive_button), Event.Positive)
            negative(Text.Resource(R.string.dialog_negative_button), Event.Negative)
        }
    }
)
