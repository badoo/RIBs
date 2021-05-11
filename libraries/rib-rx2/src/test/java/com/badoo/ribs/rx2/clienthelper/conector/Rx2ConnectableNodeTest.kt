package com.badoo.ribs.rx2.clienthelper.conector

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestNode
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output1
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output2
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test


class Rx2ConnectableNodeTest {

    private lateinit var parent: Rx2ConnectableTestNode
    private val testObserver = TestObserver<Output>()
    private val changesAwarePlugin = object : SubtreeChangeAware {
        override fun onChildAttached(child: Node<*>) {
            (child as Rx2ConnectableTestNode).output.subscribe {
                testObserver.onNext(it)
            }
        }
    }

    @Before
    fun setUp() {
        parent = Rx2ConnectableTestNode(plugins = listOf(changesAwarePlugin))
    }

    @After
    fun tearDown() {
        testObserver.dispose()
    }

    @Test
    fun `WHEN child emit some output before it is attached to parent THEN parent receive the output after child attach finished`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        parent.attachChildNode(children)

        testObserver.assertValue(Output1)
    }

    @Test
    fun `WHEN child is attached and emit some output THEN parent receive the exact output`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        parent.attachChildNode(children)
        children.output.accept(Output1)

        testObserver.assertValueCount(1)
        testObserver.assertValue(Output1)
    }

    @Test
    fun `WHEN child emit multiple outputs before it is attached to parent THEN parent receive the output after attach finished in correct order`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        children.output.accept(Output2)
        parent.attachChildNode(children)

        testObserver.assertValueAt(0, Output1)
        testObserver.assertValueAt(1, Output2)
    }

    @Test
    fun `WHEN child emit output before it is attached to parent and then after it is attached to parent THEN parent receive the output in correct order`() {
        val children = Rx2ConnectableTestNode(parent = parent)

        children.output.accept(Output1)
        parent.attachChildNode(children)
        children.output.accept(Output2)

        testObserver.assertValueAt(0, Output1)
        testObserver.assertValueAt(1, Output2)
    }
}
