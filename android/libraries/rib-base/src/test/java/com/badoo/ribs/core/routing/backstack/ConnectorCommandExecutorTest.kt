package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.NodeConnector
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.BackStackManager.State
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.android.parcel.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConnectorCommandExecutorTest {

    sealed class Configuration : Parcelable {
        @Parcelize object C1 : Configuration()
        @Parcelize object C2 : Configuration()
    }

    private lateinit var executor: ConnectorCommandExecutor<Configuration>
    private lateinit var resolver: (Configuration) -> RoutingAction<*>
    private lateinit var connector: NodeConnector

    private lateinit var routingActionViewParentedNodes: RoutingAction<*>
    private lateinit var routingActionExternalNodes: RoutingAction<*>
    private lateinit var node1_1: Node<*>
    private lateinit var node1_2: Node<*>
    private lateinit var node1_3: Node<*>
    private lateinit var node2_1: Node<*>
    private lateinit var node2_2: Node<*>
    private lateinit var nodesViewParented: List<NodeDescriptor>
    private lateinit var nodesExternal: List<NodeDescriptor>
    private lateinit var nodeFactoriesViewParented: List<() -> NodeDescriptor>
    private lateinit var nodeFactoriesExternal: List<() -> NodeDescriptor>
    private lateinit var backStackStateSubject: PublishRelay<State<Configuration>>

    @Before
    fun setUp() {
        node1_1 = mock()
        node1_2 = mock()
        node1_3 = mock()
        node2_1 = mock()
        node2_2 = mock()
        nodesViewParented = listOf(node1_1, node1_2, node1_3).map { NodeDescriptor(it, Node.ViewAttachMode.PARENT) }
        nodesExternal = listOf(node2_1, node2_2).map { NodeDescriptor(it, Node.ViewAttachMode.EXTERNAL) }
        nodeFactoriesViewParented = nodesViewParented.map { nodeDescriptor ->
            mock<() -> NodeDescriptor> { on { invoke() } doReturn nodeDescriptor }
        }
        nodeFactoriesExternal = nodesExternal.map { nodeDescriptor ->
            mock<() -> NodeDescriptor> { on { invoke() } doReturn nodeDescriptor }
        }

        routingActionViewParentedNodes = mock { on { buildNodes() } doAnswer { nodeFactoriesViewParented.map { it.invoke() } }}
        routingActionExternalNodes = mock { on { buildNodes() } doAnswer { nodeFactoriesExternal.map { it.invoke() } }}

        resolver = mock {
            on { invoke(Configuration.C1) } doReturn routingActionViewParentedNodes
            on { invoke(Configuration.C2) } doReturn routingActionExternalNodes
        }

        backStackStateSubject = PublishRelay.create<State<Configuration>>()
        connector = mock()
        executor = ConnectorCommandExecutor(resolver, connector)
    }

    // region addConfiguration
    @Test
    fun `When calling addConfiguration(), Node factories of the associated RoutingAction are invoked`() {
        executor.addConfiguration(0, Configuration.C1)
        nodeFactoriesViewParented.forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `When calling addConfiguration(), Nodes that are created are attached`() {
        executor.addConfiguration(0, Configuration.C1)
        executor.makeConfigurationActive(0)
        nodesViewParented.forEach {
            verify(connector).attachChildNode(it.node)
        }
    }

    @Test
    fun `When calling addConfiguration(), associated RoutingAction is resolved on demand`() {
        executor.addConfiguration(0, Configuration.C1)
        verify(resolver).invoke(Configuration.C1)
    }

    @Test
    fun `When calling addConfiguration(), associated RoutingAction is not yet executed`() {
        executor.addConfiguration(0, Configuration.C1)
        verify(routingActionViewParentedNodes, never()).execute()
    }
    // endregion

    // region makeConfigurationActive
    @Test
    fun `When calling makeConfigurationActive(), associated RoutingAction is executed`() {
        executor.addConfiguration(0, Configuration.C1)
        executor.makeConfigurationActive(0)
        verify(routingActionViewParentedNodes).execute()
    }

    @Test
    fun `When calling makeConfigurationActive(), attachChildView() is called on associated Nodes that are view-parented`() {
        executor.addConfiguration(0, Configuration.C1) // This configuration resolves to view-parented Nodes
        clearInvocations(connector)

        executor.makeConfigurationActive(0)
        nodesViewParented.forEach {
            verify(connector).attachChildView(it.node)
        }
        verifyNoMoreInteractions(connector)
    }

    @Test
    fun `When calling makeConfigurationActive(), attachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        executor.addConfiguration(0, Configuration.C2) // This configuration resolves to external Nodes
        clearInvocations(connector)

        executor.makeConfigurationActive(0)
        nodesViewParented.forEach {
            verify(connector, never()).attachChildView(it.node)
        }
        verifyNoMoreInteractions(connector)
    }
    // endregion

    // region makeConfigurationPassive
    @Test
    fun `When calling makeConfigurationPassive(), cleanup() is called on associated RoutingAction`() {
        executor.addConfiguration(0, Configuration.C1)
        executor.makeConfigurationPassive(0)
        verify(routingActionViewParentedNodes).cleanup()
    }

    @Test
    fun `When calling makeConfigurationPassive(), saveViewState() is called on associated Nodes`() {
        executor.addConfiguration(0, Configuration.C1)
        executor.makeConfigurationPassive(0)
        nodesViewParented.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    fun `When calling makeConfigurationPassive(), detachChildView() is called on associated Nodes that are view-parented`() {
        executor.addConfiguration(0, Configuration.C1) // This configuration resolves to view-parented Nodes
        clearInvocations(connector)

        executor.makeConfigurationPassive(0)
        nodesViewParented.forEach {
            verify(connector).detachChildView(it.node)
        }
        verifyNoMoreInteractions(connector)
    }

    @Test
    fun `When calling makeConfigurationPassive(), detachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        executor.addConfiguration(0, Configuration.C2) // This configuration resolves to external Nodes
        clearInvocations(connector)

        executor.makeConfigurationActive(0)
        nodesViewParented.forEach {
            verify(connector, never()).detachChildView(it.node)
        }
        verifyNoMoreInteractions(connector)
    }
    // endregion

    @Test
    fun `When calling makeConfigurationPassive(), Node references are kept`() {
        executor.addConfiguration(0, Configuration.C1)
        executor.makeConfigurationPassive(0)
        assertEquals(nodesViewParented, executor.nodes[0].builtNodes)
    }
    // endregion

    // region removeConfiguration
    @Test
    fun `When calling removeConfiguration(), all of its Nodes are detached regardless of view-parenting mode`() {
        executor.addConfiguration(0, Configuration.C1)
        executor.addConfiguration(1, Configuration.C2)
        clearInvocations(connector)

        executor.removeConfiguration(1)
        executor.removeConfiguration(0)
        (nodesViewParented + nodesExternal).forEach {
            inOrder(connector) {
                verify(connector).detachChildView(it.node)
                verify(connector).detachChildNode(it.node)
            }
        }
        verifyNoMoreInteractions(connector)
    }

    @Test
    fun `When calling removeConfiguration(), all Node references are cleared`() {
        executor.addConfiguration(0, Configuration.C1)
        executor.addConfiguration(1, Configuration.C2)
        executor.removeConfiguration(1)
        executor.removeConfiguration(0)
        assertEquals(true, executor.nodes.isEmpty())
    }
    // endregion
}
