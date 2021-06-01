package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.ON_SCREEN
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.VIEW_DETACHED
import com.badoo.ribs.test.util.restartActivitySync
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode3
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay.AttachNode2AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay.AttachNode3AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent2
import com.badoo.ribs.test.util.runOnMainSync
import org.junit.Test

@SuppressWarnings("LongMethod")
class RestartActivityNodesStateTest : BaseNodesTest() {

    private fun testPushTwoConfigurationThenRestart(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, _ ->
            runOnMainSync {
                router.pushIt(setup.configuration1!!)
                router.pushIt(setup.configuration2!!)
            }
            ribsRule.restartActivitySync()
        }
    }

    @Test
    fun noPermanent_singleInitial_pushContent_pushContent_restart() {
        testPushTwoConfigurationThenRestart(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3
            ),
            ExpectedState(
                node1 = VIEW_DETACHED,
                node2 = VIEW_DETACHED,
                node3 = ON_SCREEN
            )
        )
    }

    @Test
    fun noPermanent_singleInitial_pushContent_pushOverlay_restart() {
        testPushTwoConfigurationThenRestart(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                node1 = VIEW_DETACHED,
                node2 = ON_SCREEN,
                node3 = ON_SCREEN
            )
        )
    }

    @Test
    fun noPermanent_singleInitial_pushOverlay_pushOverlay_restart() {
        testPushTwoConfigurationThenRestart(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                node1 = ON_SCREEN,
                node2 = ON_SCREEN,
                node3 = ON_SCREEN
            )
        )
    }

    @Test
    fun multiplePermanent_singleInitial_pushContent_pushContent_restart() {
        testPushTwoConfigurationThenRestart(
            When(
                permanentParts = listOf(
                    Permanent1,
                    Permanent2
                ),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN,
                permanentNode2 = ON_SCREEN,
                node1 = VIEW_DETACHED,
                node2 = VIEW_DETACHED,
                node3 = ON_SCREEN
            )
        )
    }

    @Test
    fun multiplePermanent_singleInitial_pushContent_pushOverlay_restart() {
        testPushTwoConfigurationThenRestart(
            When(
                permanentParts = listOf(
                    Permanent1,
                    Permanent2
                ),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN,
                permanentNode2 = ON_SCREEN,
                node1 = VIEW_DETACHED,
                node2 = ON_SCREEN,
                node3 = ON_SCREEN
            )
        )
    }

    @Test
    fun multiplePermanent_singleInitial_pushOverlay_pushOverlay_restart() {
        testPushTwoConfigurationThenRestart(
            When(
                permanentParts = listOf(
                    Permanent1,
                    Permanent2
                ),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN,
                permanentNode2 = ON_SCREEN,
                node1 = ON_SCREEN,
                node2 = ON_SCREEN,
                node3 = ON_SCREEN
            )
        )
    }
}
