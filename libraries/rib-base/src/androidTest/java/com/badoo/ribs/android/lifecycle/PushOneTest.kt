package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.ON_SCREEN
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.VIEW_DETACHED
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay.AttachNode2AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent1
import com.badoo.ribs.test.util.runOnMainSync
import org.junit.Test

class PushOneTest : BaseNodesTest() {

    private fun pushOneConfiguration(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { router, _ ->
            runOnMainSync {
                router.pushIt(setup.configuration1!!)
            }
        }
    }

    @Test
    fun noPermanent_singleInitial_pushContent() {
        pushOneConfiguration(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2
            ),
            ExpectedState(
                node1 = VIEW_DETACHED,
                node2 = ON_SCREEN
            )
        )
    }

    @Test
    fun noPermanent_singleInitial_pushOverlay() {
        pushOneConfiguration(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay
            ),
            ExpectedState(
                node1 = ON_SCREEN,
                node2 = ON_SCREEN
            )
        )
    }

    @Test
    fun singlePermanent_singleInitial_pushContent() {
        pushOneConfiguration(
            When(
                permanentParts = listOf(Permanent1),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN,
                node1 = VIEW_DETACHED,
                node2 = ON_SCREEN
            )
        )
    }

    @Test
    fun singlePermanent_singleInitial_pushOverlay() {
        pushOneConfiguration(
            When(
                permanentParts = listOf(Permanent1),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN,
                node1 = ON_SCREEN,
                node2 = ON_SCREEN
            )
        )
    }
}
