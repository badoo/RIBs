package com.badoo.ribs.samples.dialogs.dialogtypes

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.samples.dialogs.R

class RibDialog : Dialog<Dialog.Event>(
        {
            title = Text.Resource(R.string.rib_dialog_title)
            /**
             * We can provide another rib builder here and use within the dialog:
             *  ribFactory {
             *      dummyBuilder.build(it)
             *  }
             */
            buttons {
                positive(Text.Resource(R.string.dialog_positive_button), Event.Positive)
                negative(Text.Resource(R.string.dialog_negative_button), Event.Negative)
            }
        }
)