package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.test.util.runOnMainSync

class PushTwoPopOneStopStartResumeTest : PushTwoPopOneTest() {

    override fun pushTwoConfigurationAndPop(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, rootNode ->
            runOnMainSync {
                rootNode.onStop()
                router.pushIt(setup.configuration1!!)
                rootNode.onStart()
                router.pushIt(setup.configuration2!!)
                rootNode.onResume()
                router.popBackStack()
            }
        }
    }
}
