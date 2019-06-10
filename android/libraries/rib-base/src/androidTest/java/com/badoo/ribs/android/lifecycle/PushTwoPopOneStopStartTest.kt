package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.DETACHED
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.VIEW_DETACHED
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.ON_SCREEN
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.ON_SCREEN_PAUSED
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode3
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay.AttachNode2AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay.AttachNode3AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent2
import com.badoo.ribs.test.util.runOnMainSync
import org.junit.Test

class PushTwoPopOneStopStartTest : PushTwoPopOnePauseTest() {

    override fun pushTwoConfigurationAndPop(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, rootNode ->
            runOnMainSync {
                rootNode.onStop()
                router.pushIt(setup.pushConfiguration1!!)
                rootNode.onStart()
                router.pushIt(setup.pushConfiguration2!!)
                router.popBackStack()
            }
        }
    }
}
