package com.badoo.ribs.test.util.ribs

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.Dialog


class TestRibDialog(
    builder: (Bundle?) -> Node<*>
) : Dialog<Dialog.Event>({
    ribFactory(builder)
})
