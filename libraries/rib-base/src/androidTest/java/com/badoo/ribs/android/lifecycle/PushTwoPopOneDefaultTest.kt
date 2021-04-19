package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.test.util.runOnMainSync

class PushTwoPopOneDefaultTest : PushTwoPopOneTest() {

    override fun pushTwoConfigurationAndPop(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { backStack, _ ->
            runOnMainSync {
                backStack.pushIt(setup.configuration1!!)
                backStack.pushIt(setup.configuration2!!)
                backStack.popBackStack()
            }
        }
    }
}
