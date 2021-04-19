package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.DETACHED
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.ON_SCREEN
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.VIEW_DETACHED
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode3
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay.AttachNode2AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Overlay.AttachNode3AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent2
import org.junit.Test

abstract class PushTwoPopOneTest : BaseNodesTest() {

    protected abstract fun pushTwoConfigurationAndPop(setup: When, expectedState: ExpectedState)

    @Test
    fun noPermanent_singleInitial_pushContent_pushContent_pop() {
        pushTwoConfigurationAndPop(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3
            ),
            ExpectedState(
                node1 = VIEW_DETACHED, // Next content should cause view detach on first
                node2 = ON_SCREEN,     // This should be restored after pop
                node3 = DETACHED       // This should be popped
            )
        )
    }

    @Test
    fun noPermanent_singleInitial_pushContent_pushOverlay_pop() {
        pushTwoConfigurationAndPop(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                node1 = VIEW_DETACHED, // Next content should cause view detach on first
                node2 = ON_SCREEN,     // This should be restored after pop
                node3 = DETACHED       // This should be popped
            )
        )
    }

    @Test
    fun noPermanent_singleInitial_pushOverlay_pushOverlay_pop() {
        pushTwoConfigurationAndPop(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                node1 = ON_SCREEN, // This should be restored after pop (next one is overlay)
                node2 = ON_SCREEN, // This should be restored after pop
                node3 = DETACHED   // This should be popped
            )
        )
    }

    @Test
    fun noPermanent_singleInitial_pushOverlay_pushContent_pop() {
        pushTwoConfigurationAndPop(
            When(
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay,
                configuration2 = AttachNode3
            ),
            ExpectedState(
                node1 = ON_SCREEN, // This should be restored after pop (next one is overlay)
                node2 = ON_SCREEN, // This should be restored after pop
                node3 = DETACHED   // This should be popped
            )
        )
    }

    @Test
    fun multiplePermanent_singleInitial_pushContent_pushContent_pop() {
        pushTwoConfigurationAndPop(
            When(
                permanentParts = listOf(Permanent1, Permanent2),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN, // This should always be on screen
                permanentNode2 = ON_SCREEN, // This should always be on screen
                node1 = VIEW_DETACHED,      // Next content should cause view detach on first
                node2 = ON_SCREEN,          // This should be restored after pop
                node3 = DETACHED            // This should be popped
            )
        )
    }

    @Test
    fun multiplePermanent_singleInitial_pushContent_pushOverlay_pop() {
        pushTwoConfigurationAndPop(
            When(
                permanentParts = listOf(Permanent1, Permanent2),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN, // This should always be on screen
                permanentNode2 = ON_SCREEN, // This should always be on screen
                node1 = VIEW_DETACHED,      // Second content should cause view detach on first
                node2 = ON_SCREEN,          // This should be restored after pop
                node3 = DETACHED            // This should be popped
            )
        )
    }

    @Test
    fun multiplePermanent_singleInitial_pushOverlay_pushOverlay_pop() {
        pushTwoConfigurationAndPop(
            When(
                permanentParts = listOf(Permanent1, Permanent2),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay,
                configuration2 = AttachNode3AsOverlay
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN, // This should always be on screen
                permanentNode2 = ON_SCREEN, // This should always be on screen
                node1 = ON_SCREEN,          // This should be restored after pop (next one is overlay)
                node2 = ON_SCREEN,          // This should be restored after pop
                node3 = DETACHED            // This should be popped
            )
        )
    }

    @Test
    fun multiplePermanent_singleInitial_pushOverlay_pushContent_pop() {
        pushTwoConfigurationAndPop(
            When(
                permanentParts = listOf(Permanent1, Permanent2),
                initialConfiguration = AttachNode1,
                configuration1 = AttachNode2AsOverlay,
                configuration2 = AttachNode3
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN, // This should always be on screen
                permanentNode2 = ON_SCREEN, // This should always be on screen
                node1 = ON_SCREEN,          // This should be restored after pop (next one is overlay)
                node2 = ON_SCREEN,          // This should be restored after pop
                node3 = DETACHED            // This should be popped
            )
        )
    }
}
