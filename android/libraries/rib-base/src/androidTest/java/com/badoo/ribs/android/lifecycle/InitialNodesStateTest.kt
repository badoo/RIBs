package com.badoo.ribs.android.lifecycle

import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.android.lifecycle.BaseNodesTest.TestNode.NODE_1
import com.badoo.ribs.android.lifecycle.BaseNodesTest.TestNode.NODE_2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.AttachNode1And2
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration.NoOp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class InitialNodesStateTest(private val test: Pair<When, Then>) : BaseNodesTest(
    initialConfiguration = test.first.initialConfiguration,
    permanentParts = test.first.permanentParts
) {

    companion object {
        @JvmStatic
        @Suppress("LongMethod")
        @Parameters(name = "{0}")
        fun data() = listOf(

            When(initialConfiguration = NoOp, permanentParts = emptyList())
                to Then(node1 = null, node2 = null),

            When(initialConfiguration = AttachNode1, permanentParts = emptyList())
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = null),

            When(initialConfiguration = NoOp, permanentParts = listOf(NODE_1))
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = null),

            When(initialConfiguration = AttachNode1, permanentParts = listOf(NODE_2))
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true)),

            When(initialConfiguration = AttachNode1And2, permanentParts = emptyList())
                to Then(node1 = NodeState(attached = true, viewAttached = true), node2 = NodeState(attached = true, viewAttached = true))
        )
    }

    @get:Rule
    val ribsRule = RibsRule { rootProvider.invoke() }

    @Test
    fun whenRootIsCreated() {
        makeAssertions(test.second)
    }

    class When(
        val initialConfiguration: Configuration,
        val permanentParts: List<TestNode>
    ) {
        override fun toString() = "initial configuration is ${initialConfiguration::class.java.simpleName} " +
            "and permanent parts = [${permanentParts.joinToString()}]"
    }
}
