package com.badoo.ribs.android.lifecycle

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.android.lifecycle.BaseNodesTest.TestNode.NODE_1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1And2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1AsDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1AsDialogAndOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2AsDialog
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2AsDialogAndOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode2AsOverlay
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.NoOp
import com.badoo.ribs.test.util.runOnMainSync
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class PushOneConfigurationNodesStateTest(private val test: Pair<When, Then>) : BaseNodesTest(
    initialConfiguration = test.first.initialConfiguration,
    permanentParts = test.first.permanentParts
) {

    companion object {
        @JvmStatic
        @Suppress("LongMethod")
        @Parameters(name = "{0}")
        fun data() = listOf(
            When(pushConfiguration = AttachNode1)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = null),

            When(initialConfiguration = AttachNode1, pushConfiguration = AttachNode2)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1, pushConfiguration = AttachNode2AsDialog)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(initialConfiguration = AttachNode1, pushConfiguration = AttachNode2AsOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1, pushConfiguration = AttachNode2AsDialogAndOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = false)),


            When(pushConfiguration = AttachNode1AsDialog)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = null),

            When(initialConfiguration = AttachNode1AsDialog, pushConfiguration = AttachNode2)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1AsDialog, pushConfiguration = AttachNode2AsDialog)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(initialConfiguration = AttachNode1AsDialog, pushConfiguration = AttachNode2AsOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1AsDialog, pushConfiguration = AttachNode2AsDialogAndOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),


            When(pushConfiguration = AttachNode1AsOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = null),

            When(initialConfiguration = AttachNode1AsOverlay, pushConfiguration = AttachNode2)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1AsOverlay, pushConfiguration = AttachNode2AsDialog)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(initialConfiguration = AttachNode1AsOverlay, pushConfiguration = AttachNode2AsOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1AsOverlay, pushConfiguration = AttachNode2AsDialogAndOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = false)),


            When(pushConfiguration = AttachNode1AsDialogAndOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = null),

            When(initialConfiguration = AttachNode1AsDialogAndOverlay, pushConfiguration = AttachNode2)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1AsDialogAndOverlay, pushConfiguration = AttachNode2AsDialog)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),

            When(initialConfiguration = AttachNode1AsDialogAndOverlay, pushConfiguration = AttachNode2AsOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1AsDialogAndOverlay, pushConfiguration = AttachNode2AsDialogAndOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = false), node2 = NodeState(attached = true, viewAttached = false)),


            When(permanentParts = listOf(NODE_1), pushConfiguration = AttachNode2)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true)),

            When(permanentParts = listOf(NODE_1), pushConfiguration = AttachNode2AsOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true)),

            When(permanentParts = listOf(NODE_1), pushConfiguration = AttachNode2AsDialog)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = false)),

            When(permanentParts = listOf(NODE_1), pushConfiguration = AttachNode2AsDialogAndOverlay)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = false)),


            When(pushConfiguration = AttachNode1And2)
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true))
        )
    }

    @get:Rule
    val ribsRule = RibsRule { rootProvider.invoke() }

    @Test
    fun pushOneConfiguration() {
        runOnMainSync {
            router.push(test.first.pushConfiguration)
        }

        makeAssertions(test.second)
    }

    class When(
        val pushConfiguration: Configuration,
        val initialConfiguration: Configuration = NoOp,
        val permanentParts: List<TestNode> = emptyList()
    ) {
        override fun toString() = "initial configuration is ${initialConfiguration::class.java.simpleName} " +
            "push ${pushConfiguration::class.java.simpleName}, " +
            "and permanent parts = [${permanentParts.joinToString()}]"
    }
}
