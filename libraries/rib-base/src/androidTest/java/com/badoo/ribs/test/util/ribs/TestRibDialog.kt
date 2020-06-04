package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.core.builder.NodeFactory
import com.badoo.ribs.android.dialog.Dialog


class TestRibDialog(
    nodeFactory: NodeFactory
) : Dialog<Dialog.Event>({
    nodeFactory(nodeFactory = nodeFactory)
})
