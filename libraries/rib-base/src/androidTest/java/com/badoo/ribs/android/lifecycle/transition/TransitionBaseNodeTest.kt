package com.badoo.ribs.android.lifecycle.transition

import com.badoo.ribs.android.lifecycle.BaseNodesTest
import com.badoo.ribs.routing.transition.handler.Slider
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.test.util.ribs.root.TestRootRouter

open class TransitionBaseNodeTest: BaseNodesTest() {

    override val transitionHandler: TransitionHandler<TestRootRouter.Configuration>?
        get() = Slider()
}
