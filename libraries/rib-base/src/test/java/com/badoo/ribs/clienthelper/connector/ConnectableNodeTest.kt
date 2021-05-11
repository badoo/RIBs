package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.test.helper.ConnectableTestNode
import com.badoo.ribs.test.helper.ConnectableTestRib
import io.reactivex.observers.TestObserver
import org.junit.Test


class ConnectableNodeTest {

    @Test
    fun `WHEN child emit some output before it is attached to parent THEN parent receive the output after child attach finished`() {
        val testObserver = TestObserver<ConnectableTestRib.Output>()
        val changesAwarePlugin = object : SubtreeChangeAware {
            override fun onChildAttached(child: Node<*>) {
                (child as ConnectableTestNode).output.observe {
                    testObserver.onNext(it)
                }
            }
        }
        val parent = TestNode(viewFactory = null, plugins = listOf(changesAwarePlugin))
        val children = ConnectableTestNode(parent = parent)

        children.output.emit(ConnectableTestRib.Output.Output1)
        parent.attachChildNode(children)

        testObserver.assertValue(ConnectableTestRib.Output.Output1)
    }

    @Test
    fun `WHEN child is attached and emit some output THEN parent receive the exact output`() {
        val testObserver = TestObserver<ConnectableTestRib.Output>()
        val changesAwarePlugin = object : SubtreeChangeAware {
            override fun onChildAttached(child: Node<*>) {
                (child as ConnectableTestNode).output.observe {
                    testObserver.onNext(it)
                }
            }
        }
        val parent = TestNode(viewFactory = null, plugins = listOf(changesAwarePlugin))
        val children = ConnectableTestNode(parent = parent)

        parent.attachChildNode(children)
        children.output.emit(ConnectableTestRib.Output.Output1)

        testObserver.assertValueCount(1)
        testObserver.assertValue(ConnectableTestRib.Output.Output1)
    }

    @Test
    fun `WHEN child emit multiple outputs before it is attached to parent THEN parent receive the output after attach finished in correct order`() {
        val testObserver = TestObserver<ConnectableTestRib.Output>()
        val changesAwarePlugin = object : SubtreeChangeAware {
            override fun onChildAttached(child: Node<*>) {
                (child as ConnectableTestNode).output.observe {
                    testObserver.onNext(it)
                }
            }
        }
        val parent = TestNode(viewFactory = null, plugins = listOf(changesAwarePlugin))
        val children = ConnectableTestNode(parent = parent)

        children.output.emit(ConnectableTestRib.Output.Output1)
        children.output.emit(ConnectableTestRib.Output.Output2)
        parent.attachChildNode(children)

        testObserver.assertValueAt(0, ConnectableTestRib.Output.Output1)
        testObserver.assertValueAt(1, ConnectableTestRib.Output.Output2)
    }

    @Test
    fun `WHEN child emit output before it is attached to parent and then after it is attached to parent THEN parent receive the output in correct order`() {
        val testObserver = TestObserver<ConnectableTestRib.Output>()
        val changesAwarePlugin = object : SubtreeChangeAware {
            override fun onChildAttached(child: Node<*>) {
                (child as ConnectableTestNode).output.observe {
                    testObserver.onNext(it)
                }
            }
        }
        val parent = TestNode(viewFactory = null, plugins = listOf(changesAwarePlugin))
        val children = ConnectableTestNode(parent = parent)

        children.output.emit(ConnectableTestRib.Output.Output1)
        parent.attachChildNode(children)
        children.output.emit(ConnectableTestRib.Output.Output2)

        testObserver.assertValueAt(0, ConnectableTestRib.Output.Output1)
        testObserver.assertValueAt(1, ConnectableTestRib.Output.Output2)
    }
}
