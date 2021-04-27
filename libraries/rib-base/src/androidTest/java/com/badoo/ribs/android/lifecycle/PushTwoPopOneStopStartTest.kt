package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.test.util.runOnMainSync

class PushTwoPopOneStopStartTest : PushTwoPopOnePauseTest() {

    override fun pushTwoConfigurationAndPop(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, rootNode ->
            runOnMainSync {
                rootNode.onStop()
                router.pushIt(setup.configuration1!!)
                rootNode.onStart()
                router.pushIt(setup.configuration2!!)
                router.popBackStack()
            }
        }
    }
}
