package com.badoo.ribs.android.lifecycle.transition

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import com.badoo.ribs.test.util.runOnMainSync
import org.junit.Test

@Suppress("FunctionNaming")
class ReplaceTwoWithTransitionTest : TransitionBaseNodeTest() {

    private fun replaceTwoConfigurations(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, _ ->
            runOnMainSync {
                router.replace(setup.configuration1!!)
                router.replace(setup.configuration2!!)
            }
        }
    }

    @Test
    fun replace_initial_configuration_with_new_and_replace_again_with_initial_configuration() {
        replaceTwoConfigurations(
            When(
                initialConfiguration = TestRootRouter.Configuration.Content.AttachNode1,
                configuration1 = TestRootRouter.Configuration.Content.AttachNode2,
                configuration2 = TestRootRouter.Configuration.Content.AttachNode1
            ),
            ExpectedState(
                node1 = NodeState.ON_SCREEN_RESUMED,
                node2 = NodeState.DETACHED,
            )
        )
    }
}
