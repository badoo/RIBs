package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.android.dialog.RibFactory
import com.badoo.ribs.android.dialog.Dialog


class TestRibDialog(
    ribFactory: RibFactory
) : Dialog<Dialog.Event>({
    ribFactory(ribFactory = ribFactory)
})
