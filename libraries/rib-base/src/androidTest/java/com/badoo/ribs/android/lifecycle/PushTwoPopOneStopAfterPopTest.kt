package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.test.util.runOnMainSync

class PushTwoPopOneStopAfterPopTest : PushTwoPopOneStopTest() {

    override fun pushTwoConfigurationAndPop(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, rootNode ->
            runOnMainSync {
                router.pushIt(setup.configuration1!!)
                router.pushIt(setup.configuration2!!)
                router.popBackStack()
                rootNode.onStop()
            }
        }
    }
}
