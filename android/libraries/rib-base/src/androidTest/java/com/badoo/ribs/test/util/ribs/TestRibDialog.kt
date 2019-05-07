package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.Dialog


class TestRibDialog(
    builder: () -> Node<*>
) : Dialog<Dialog.Event>({
    ribFactory(builder)
})
