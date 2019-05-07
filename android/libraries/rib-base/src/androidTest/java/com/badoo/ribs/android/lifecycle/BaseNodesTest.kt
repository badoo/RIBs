package com.badoo.ribs.android.lifecycle

import com.badoo.ribs.android.lifecycle.BaseNodesTest.TestNode.NODE_1
import com.badoo.ribs.android.lifecycle.BaseNodesTest.TestNode.NODE_2
import com.badoo.ribs.core.Node
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.test.util.NoOpDialogLauncher
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import org.assertj.core.api.Java6Assertions.assertThat

abstract class BaseNodesTest(
    initialConfiguration: TestRootRouter.Configuration = TestRootRouter.Configuration.NoOp,
    permanentParts: List<TestNode> = emptyList(),
    dialogLauncher: DialogLauncher = NoOpDialogLauncher()
) {

    protected val rootProvider = TestRoot.Provider(
        initialConfiguration = initialConfiguration,
        permanentParts = permanentParts.map { it.toNodeBuilder() },
        dialogLauncher = dialogLauncher
    )

    protected val router: TestRootRouter
        get() = rootProvider.rootNode?.getRouter() as TestRootRouter

    class NodeState(
        val attached: Boolean,
        val viewAttached: Boolean
    ) {
        override fun toString() = "attached = $attached, view attached = $viewAttached"
    }

    class ExpectedState(
        val node1: NodeState?,
        val node2: NodeState?
    ) {
        override fun toString() = "*then* node1 should be [$node1] and node2 should be [$node2]"
    }

    enum class TestNode {
        NODE_1,
        NODE_2
    }

    private fun TestNode.toNodeBuilder(): () -> Node<*> = when (this) {
        NODE_1 -> ({ rootProvider.childNode1Builder() })
        NODE_2 -> ({ rootProvider.childNode2Builder() })
    }

    protected fun makeAssertions(expected: ExpectedState) {
        assertThat(rootProvider.childNode1?.isAttached).describedAs("is child node 1 attached").isEqualTo(expected.node1?.attached)
        assertThat(rootProvider.childNode1?.isViewAttached).describedAs("is child node 1 view attached").isEqualTo(expected.node1?.viewAttached)
        assertThat(rootProvider.childNode2?.isAttached).describedAs("is child node 2 attached").isEqualTo(expected.node2?.attached)
        assertThat(rootProvider.childNode2?.isViewAttached).describedAs("is child node 2 view attached").isEqualTo(expected.node2?.viewAttached)
    }
}
