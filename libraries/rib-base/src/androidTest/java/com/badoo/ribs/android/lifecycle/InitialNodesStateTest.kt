package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState.Companion.ON_SCREEN
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.AttachNode1And2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Content.NoOp
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.Permanent.Permanent2
import org.junit.Test

class InitialNodesStateTest : BaseNodesTest() {

    private fun whenRootIsCreated(setup: When, expectedState: ExpectedState) {
        test(setup, expectedState) { _, _ ->
            // no special operations here, just test initial
        }
    }

    @Test
    fun noInitial_noPermanent() {
        whenRootIsCreated(
            When(initialConfiguration = NoOp, permanentParts = emptyList()),
            ExpectedState(
                permanentNode1 = null,
                permanentNode2 = null,
                node1 = null,
                node2 = null,
                node3 = null
            )
        )
    }

    @Test
    fun singleInitial_noPermanent() {
        whenRootIsCreated(
            When(initialConfiguration = AttachNode1, permanentParts = emptyList()),
            ExpectedState(
                node1 = ON_SCREEN
            )
        )
    }

    @Test
    fun noInitial_singlePermanent() {
        whenRootIsCreated(
            When(initialConfiguration = NoOp, permanentParts = listOf(Permanent1)),
            ExpectedState(
                permanentNode1 = ON_SCREEN
            )
        )
    }

    @Test
    fun singleInitial_singlePermanent() {
        whenRootIsCreated(
            When(initialConfiguration = AttachNode1, permanentParts = listOf(Permanent2)),
            ExpectedState(
                permanentNode2 = ON_SCREEN,
                node1 = ON_SCREEN
            )
        )
    }

    @Test
    fun multipleInitial_noPermanent() {
        whenRootIsCreated(
            When(initialConfiguration = AttachNode1And2, permanentParts = emptyList()),
            ExpectedState(
                node1 = ON_SCREEN,
                node2 = ON_SCREEN
            )
        )
    }

    @Test
    fun multipleInitial_multiplePermanent() {
        whenRootIsCreated(
            When(
                initialConfiguration = AttachNode1And2,
                permanentParts = listOf(Permanent1, Permanent2)
            ),
            ExpectedState(
                permanentNode1 = ON_SCREEN,
                permanentNode2 = ON_SCREEN,
                node1 = ON_SCREEN,
                node2 = ON_SCREEN
            )
        )
    }
}
