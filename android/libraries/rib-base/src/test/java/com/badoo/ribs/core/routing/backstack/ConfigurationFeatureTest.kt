package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.BackStackFeature.State
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Individual.*
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.Global.*
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import kotlinx.android.parcel.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConfigurationFeatureTest {

    sealed class Configuration : Parcelable {
        @Parcelize object C1 : Configuration()
        @Parcelize object C2 : Configuration()
    }

    private lateinit var feature: ConfigurationFeature<Configuration>
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
        feature = ConfigurationFeature(mock(), resolver, parentNode)
    }

    // region Add
    @Test
    fun `On Add, Node factories of the associated RoutingAction are invoked`() {
        feature.accept(Add(0, Configuration.C1))
        nodeFactoriesViewParented.forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `On Add, Nodes that are created are attached`() {
        feature.accept(Add(0, Configuration.C1))
        nodesViewParented.forEach {
            verify(parentNode).attachChildNode(it.node, null)
        }
    }

    @Test
    fun `On Add, associated RoutingAction is resolved on demand`() {
        feature.accept(Add(0, Configuration.C1))
        verify(resolver).invoke(Configuration.C1)
    }

    @Test
    fun `On Add, associated RoutingAction is not yet executed`() {
        feature.accept(Add(0, Configuration.C1))
        verify(routingActionViewParentedNodes, never()).execute()
    }
    // endregion

    // region Activate
    @Test
    fun `On Activate, associated RoutingAction is executed`() {
        feature.accept(Add(0, Configuration.C1))
        feature.accept(Activate(0))
        verify(routingActionViewParentedNodes).execute()
    }

    @Test
    fun `On Activate, attachChildView() is called on associated Nodes that are view-parented`() {
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Activate(0))
        nodesViewParented.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }

    @Test
    fun `On Activate, attachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        feature.accept(Add(0, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(0))
        nodesViewParented.forEach {
            verify(parentNode, never()).attachChildView(it.node)
        }
    }
    // endregion

    // region Deactivate
    @Test
    fun `On Deactivate, cleanup() is called on associated RoutingAction`() {
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(0))
        verify(routingActionViewParentedNodes).cleanup()
    }

    @Test
    fun `On Deactivate, saveViewState() is called on associated Nodes`() {
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(0))
        nodesViewParented.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    fun `On Deactivate, detachChildView() is called on associated Nodes that are view-parented`() {
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(0))
        nodesViewParented.forEach {
            verify(parentNode).detachChildView(it.node)
        }
    }

    @Test
    fun `On Deactivate, detachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        feature.accept(Add(0, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Deactivate(0))
        nodesViewParented.forEach {
            verify(parentNode, never()).detachChildView(it.node)
        }
    }

    @Test
    fun `On Deactivate, Node references are kept`() {
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(0))
        val configurationContext = feature.state.pool[0]
        assertEquals(true, configurationContext is Resolved)
        assertEquals(nodesViewParented, (configurationContext as? Resolved)?.nodes)
    }
    // endregion

    // region Remove
    @Test
    fun `On Remove, all of its Nodes are detached regardless of view-parenting mode`() {
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(1, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Remove(1))
        feature.accept(Remove(0))

        (nodesViewParented + nodesExternal).forEach {
            inOrder(parentNode) {
                verify(parentNode).detachChildView(it.node)
                verify(parentNode).detachChildNode(it.node)
            }
        }
    }

    @Test
    fun `On Remove, all Node references are cleared`() {
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(1, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Remove(1))
        feature.accept(Remove(0))

        feature.state.pool.forEach { (_, value) ->
            assertEquals(true, (value as? Resolved)?.nodes)
        }
    }
    // endregion

    // region Sleep
    @Test
    fun `On Sleep after WakeUp, cleanup() is called on associated RoutingAction`() {
        feature.accept(WakeUp())
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(1, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(0))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        verify(routingActionViewParentedNodes).cleanup()
    }

    @Test
    // FIXME more configurations to test "every ACTIVE node" part
    fun `On Sleep after WakeUp, saveViewState() is called on every ACTIVE node`() {
        feature.accept(WakeUp())
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(1, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(0))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        nodesViewParented.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    // FIXME more configurations to test "every ACTIVE node" part
    fun `On Sleep after WakeUp, detachChildView() is called on every ACTIVE node that are view-parented`() {
        feature.accept(WakeUp())
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(1, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(0))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        nodesViewParented.forEach {
            verify(parentNode).detachChildView(it.node)
        }
    }
    // endregion

    // region WakeUp
    @Test
    fun `On WakeUp after Sleep, execute() is called on associated RoutingAction`() {
        feature.accept(WakeUp())
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(1, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(0))
        feature.accept(Sleep())
        clearInvocations(routingActionViewParentedNodes)
        feature.accept(WakeUp())

        verify(routingActionViewParentedNodes).execute()
    }

    @Test
    // FIXME more configurations to test "every ACTIVE node" part
    fun `On WakeUp after Sleep, attachChildView() is called on every ACTIVE node that are view-parented`() {
        feature.accept(WakeUp())
        feature.accept(Add(0, Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(1, Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(0))
        feature.accept(Sleep())
        clearInvocations(parentNode)
        feature.accept(WakeUp())

        nodesViewParented.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }
    // endregion
}
