package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.RibFactory

class TestRibDialog(
    ribFactory: RibFactory
) : Dialog<Dialog.Event>({
    ribFactory(ribFactory = ribFactory)
})
