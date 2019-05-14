package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.BackStackManager.State
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.*
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
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

class ConfigurationHandlerTest {

    sealed class Configuration : Parcelable {
        @Parcelize object C1 : Configuration()
        @Parcelize object C2 : Configuration()
    }

    private lateinit var handler: ConfigurationHandler<Configuration>
    private lateinit var resolver: (Configuration) -> RoutingAction<*>
    private lateinit var parentNode: Node<*>

    private lateinit var routingActionViewParentedNodes: RoutingAction<*>
    private lateinit var routingActionExternalNodes: RoutingAction<*>
    private lateinit var node1_1: Node<*>
    private lateinit var node1_2: Node<*>
    private lateinit var node1_3: Node<*>
    private lateinit var node2_1: Node<*>
    private lateinit var node2_2: Node<*>
    private lateinit var nodesViewParented: List<Node.Descriptor>
    private lateinit var nodesExternal: List<Node.Descriptor>
    private lateinit var nodeFactoriesViewParented: List<() -> Node.Descriptor>
    private lateinit var nodeFactoriesExternal: List<() -> Node.Descriptor>
    private lateinit var backStackStateSubject: PublishRelay<State<Configuration>>

    @Before
    fun setUp() {
        node1_1 = mock()
        node1_2 = mock()
        node1_3 = mock()
        node2_1 = mock()
        node2_2 = mock()
        nodesViewParented = listOf(node1_1, node1_2, node1_3).map { Node.Descriptor(it, Node.ViewAttachMode.PARENT) }
        nodesExternal = listOf(node2_1, node2_2).map { Node.Descriptor(it, Node.ViewAttachMode.EXTERNAL) }
        nodeFactoriesViewParented = nodesViewParented.map { nodeDescriptor ->
            mock<() -> Node.Descriptor> { on { invoke() } doReturn nodeDescriptor }
        }
        nodeFactoriesExternal = nodesExternal.map { nodeDescriptor ->
            mock<() -> Node.Descriptor> { on { invoke() } doReturn nodeDescriptor }
        }

        routingActionViewParentedNodes = mock { on { buildNodes() } doAnswer { nodeFactoriesViewParented.map { it.invoke() } }}
        routingActionExternalNodes = mock { on { buildNodes() } doAnswer { nodeFactoriesExternal.map { it.invoke() } }}

        resolver = mock {
            on { invoke(Configuration.C1) } doReturn routingActionViewParentedNodes
            on { invoke(Configuration.C2) } doReturn routingActionExternalNodes
        }

        backStackStateSubject = PublishRelay.create<State<Configuration>>()
        parentNode = mock()
        handler = ConfigurationHandler(resolver, parentNode)
    }

    // region addConfiguration
    @Test
    fun `When calling addConfiguration(), Node factories of the associated RoutingAction are invoked`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1)
            )
        )
        nodeFactoriesViewParented.forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `When calling addConfiguration(), Nodes that are created are attached`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1),
                Activate(0)
            )
        )
        nodesViewParented.forEach {
            verify(parentNode).attachChildNode(it.node, null)
        }
    }

    @Test
    fun `When calling addConfiguration(), associated RoutingAction is resolved on demand`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1)
            )
        )
        verify(resolver).invoke(Configuration.C1)
    }

    @Test
    fun `When calling addConfiguration(), associated RoutingAction is not yet executed`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1)
            )
        )
        verify(routingActionViewParentedNodes, never()).execute()
    }
    // endregion

    // region makeConfigurationActive
    @Test
    fun `When calling makeConfigurationActive(), associated RoutingAction is executed`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1),
                Activate(0)
            )
        )
        verify(routingActionViewParentedNodes).execute()
    }

    @Test
    fun `When calling makeConfigurationActive(), attachChildView() is called on associated Nodes that are view-parented`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1), // This configuration resolves to view-parented Nodes
                Activate(0)
            )
        )
        nodesViewParented.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }

    @Test
    fun `When calling makeConfigurationActive(), attachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C2), // This configuration resolves to external Nodes
                Activate(0)
            )
        )

        nodesViewParented.forEach {
            verify(parentNode, never()).attachChildView(it.node)
        }
    }
    // endregion

    // region makeConfigurationPassive
    @Test
    fun `When calling makeConfigurationPassive(), cleanup() is called on associated RoutingAction`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1),
                Deactivate(0)
            )
        )
        verify(routingActionViewParentedNodes).cleanup()
    }

    @Test
    fun `When calling makeConfigurationPassive(), saveViewState() is called on associated Nodes`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1),
                Deactivate(0)
            )
        )
        nodesViewParented.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    fun `When calling makeConfigurationPassive(), detachChildView() is called on associated Nodes that are view-parented`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1), // This configuration resolves to view-parented Nodes
                Deactivate(0)
            )
        )
        nodesViewParented.forEach {
            verify(parentNode).detachChildView(it.node)
        }
    }

    @Test
    fun `When calling makeConfigurationPassive(), detachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C2), // This configuration resolves to external Nodes
                Deactivate(0)
            )
        )

        nodesViewParented.forEach {
            verify(parentNode, never()).detachChildView(it.node)
        }
    }
    // endregion

    @Test
    fun `When calling makeConfigurationPassive(), Node references are kept`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1), // This configuration resolves to view-parented Nodes
                Deactivate(0)
            )
        )
        val configurationContext = handler.pool[0]
        assertEquals(true, configurationContext is Resolved)
        assertEquals(nodesViewParented, (configurationContext as? Resolved)?.builtNodes)
    }
    // endregion

    // region removeConfiguration
    @Test
    fun `When calling removeConfiguration(), all of its Nodes are detached regardless of view-parenting mode`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1), // This configuration resolves to view-parented Nodes
                Add(1, Configuration.C2), // This configuration resolves to external Nodes
                Remove(1),
                Remove(0)
            )
        )
        (nodesViewParented + nodesExternal).forEach {
            inOrder(parentNode) {
                verify(parentNode).detachChildView(it.node)
                verify(parentNode).detachChildNode(it.node)
            }
        }
    }

    @Test
    fun `When calling removeConfiguration(), all Node references are cleared`() {
        handler.accept(
            listOf(
                Add(0, Configuration.C1), // This configuration resolves to view-parented Nodes
                Add(1, Configuration.C2), // This configuration resolves to external Nodes
                Remove(1),
                Remove(0)
            )
        )
        handler.pool.forEach { _, value ->
            assertEquals(true, (value as? Resolved)?.builtNodes)
        }
    }
    // endregion
}
