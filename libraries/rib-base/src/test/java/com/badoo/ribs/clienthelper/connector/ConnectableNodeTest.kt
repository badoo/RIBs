package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.test.BaseConnectableNodeTest
import com.badoo.ribs.test.helper.connectable.ConnectableTestNode
import com.badoo.ribs.test.helper.connectable.ConnectableTestRib
import com.badoo.ribs.test.helper.connectable.ConnectableTestRib.Output.Output1
import com.badoo.ribs.test.helper.connectable.ConnectableTestRib.Output.Output2
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test


class ConnectableNodeTest : BaseConnectableNodeTest() {

    private lateinit var parent: TestNode
    private val testObserver = TestObserver<ConnectableTestRib.Output>()
    private val changesAwarePlugin = object : SubtreeChangeAware {
        override fun onChildAttached(child: Node<*>) {
            (child as ConnectableTestNode).output.observe {
                testObserver.onNext(it)
            }
        }
    }

    @Before
    fun setUp() {
        parent = TestNode(viewFactory = null, plugins = listOf(changesAwarePlugin))
    }

    @After
    fun tearDown() {
        testObserver.dispose()
    }

    @Test
    override fun WHEN_child_emit_some_output_before_it_is_attached_to_parent_THEN_parent_receive_the_output_after_child_attach_finished() {
        val children = ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        parent.attachChildNode(children)

        testObserver.assertValue(Output1)
    }

    @Test
    override fun `WHEN_child_is_attached_and_emit_some_output_THEN_parent_receive_the_exact_output`() {
        val children = ConnectableTestNode(parent = parent)

        parent.attachChildNode(children)
        children.output.accept(Output1)

        testObserver.assertValueCount(1)
        testObserver.assertValue(Output1)
    }

    @Test
    override fun `WHEN_child_emit_multiple_outputs_before_it_is_attached_to_parent_THEN_parent_receive_the_output_after_attach_finished_in_correct_order`() {
        val children = ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        children.output.accept(Output2)
        parent.attachChildNode(children)

        testObserver.assertValueAt(0, Output1)
        testObserver.assertValueAt(1, Output2)
    }

    @Test
    override fun `WHEN_child_emit_output_before_it_is_attached_to_parent_and_then_after_it_is_attached_to_parent_THEN_parent_receive_the_output_in_correct_order`() {
        val children = ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        parent.attachChildNode(children)
        children.output.accept(Output2)

        testObserver.assertValueAt(0, Output1)
        testObserver.assertValueAt(1, Output2)
    }
}
