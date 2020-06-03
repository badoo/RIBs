package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.test.util.runOnMainSync

class PushTwoPopOnePauseAfterPopTest : PushTwoPopOnePauseTest() {

    override fun pushTwoConfigurationAndPop(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, rootNode ->
            runOnMainSync {
                router.pushIt(setup.pushConfiguration1!!)
                router.pushIt(setup.pushConfiguration2!!)
                router.popBackStack()
                rootNode.onPause()
            }
        }
    }
}
