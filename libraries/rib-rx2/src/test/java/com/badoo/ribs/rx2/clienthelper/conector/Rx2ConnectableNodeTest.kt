package com.badoo.ribs.rx2.clienthelper.conector

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestNode
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output1
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output2
import com.badoo.ribs.test.BaseConnectableNodeTest
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class Rx2ConnectableNodeTest : BaseConnectableNodeTest() {

    private lateinit var parent: Rx2ConnectableTestNode
    private val testObserver = TestObserver<Output>()
    private val changesAwarePlugin = object : SubtreeChangeAware {
        override fun onChildAttached(child: Node<*>) {
            (child as Rx2ConnectableTestNode).output.subscribe {
                testObserver.onNext(it)
            }
        }
    }

    @BeforeEach
    fun setUp() {
        parent = Rx2ConnectableTestNode(plugins = listOf(changesAwarePlugin))
    }

    @AfterEach
    fun tearDown() {
        testObserver.dispose()
    }

    @Test
    override fun `WHEN_child_emit_some_output_before_it_is_attached_to_parent_THEN_parent_receive_the_output_after_child_attach_finished`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        parent.attachChildNode(children)

        testObserver.assertValue(Output1)
    }

    @Test
    override fun `WHEN_child_is_attached_and_emit_some_output_THEN_parent_receive_the_exact_output`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        parent.attachChildNode(children)
        children.output.accept(Output1)

        testObserver.assertValueCount(1)
        testObserver.assertValue(Output1)
    }

    @Test
    override fun `WHEN_child_emit_multiple_outputs_before_it_is_attached_to_parent_THEN_parent_receive_the_output_after_attach_finished_in_correct_order`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        children.output.accept(Output2)
        parent.attachChildNode(children)

        testObserver.assertValues(Output1, Output2)
    }

    @Test
    override fun `WHEN_child_emit_output_before_it_is_attached_to_parent_and_then_after_it_is_attached_to_parent_THEN_parent_receive_the_output_in_correct_order`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        parent.attachChildNode(children)
        children.output.accept(Output2)

        testObserver.assertValues(Output1, Output2)
    }
}
