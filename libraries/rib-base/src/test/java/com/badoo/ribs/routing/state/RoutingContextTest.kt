package com.badoo.ribs.routing.state

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.ActivationMode
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.resolver.RoutingResolver
import com.badoo.ribs.routing.state.RoutingContext.ActivationState.*
import com.badoo.ribs.routing.state.RoutingContext.Resolved
import com.badoo.ribs.routing.state.RoutingContext.Unresolved
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test

class RoutingContextTest {

    companion object {
        private const val NB_EXPECTED_NODES = 3
    }

    private val nodes: List<Node<*>> = List(NB_EXPECTED_NODES) { createMockNode() }
    private val ribs: List<Rib> = nodes.map {
        object : Rib {
            override val node: Node<*> = it
        }
    }

    // Default
    private val defaultResolution = mock<Resolution> {
        on { numberOfNodes } doReturn NB_EXPECTED_NODES
        on { buildNodes(any()) } doReturn ribs
    }
    private val defaultResolver = mock<RoutingResolver<Parcelable>> {
        on { resolve(any()) } doReturn defaultResolution
    }

    // With Anchor
    private val mockAnchor: Node<*> = createMockNode()
    private val routingActionWithAnchor = mock<Resolution> {
        on { numberOfNodes } doReturn NB_EXPECTED_NODES
        on { buildNodes(any()) } doReturn nodes
        on { anchor } doReturn mockAnchor
    }
    private val resolverWithAnchor = mock<RoutingResolver<Parcelable>> {
        on { resolve(any()) } doReturn routingActionWithAnchor
    }

    private fun createMockNode() =
        mock<Node<*>> {
            on { this.buildContext } doReturn BuildContext.root(null)
        }

    @Test
    fun `Unresolved sleep() keeps INACTIVE`() {
        val unresolved = Unresolved<Parcelable>(INACTIVE, mock())
        val afterInvocation = unresolved.sleep()
        assertEquals(INACTIVE, afterInvocation.activationState)
    }

    @Test
    fun `Unresolved sleep() keeps SLEEPING`() {
        val unresolved = Unresolved<Parcelable>(SLEEPING, mock())
        val afterInvocation = unresolved.sleep()
        assertEquals(SLEEPING, afterInvocation.activationState)
    }

    @Test
    fun `Unresolved wakeUp() keeps INACTIVE`() {
        val unresolved = Unresolved<Parcelable>(INACTIVE, mock())
        val afterInvocation = unresolved.wakeUp()
        assertEquals(INACTIVE, afterInvocation.activationState)
    }

    @Test
    fun `Unresolved resolve() keeps INACTIVE`() {
        val parentNode = createMockNode()
        val unresolved = Unresolved<Parcelable>(INACTIVE, mock())
        val resolved = unresolved.resolve(defaultResolver, parentNode)
        assertEquals(INACTIVE, resolved.activationState)
    }

    @Test
    fun `Unresolved resolve() keeps SLEEPING`() {
        val parentNode = createMockNode()
        val unresolved = Unresolved<Parcelable>(SLEEPING, mock())
        val resolved = unresolved.resolve(defaultResolver, parentNode)
        assertEquals(SLEEPING, resolved.activationState)
    }

    @Test
    fun `Unresolved resolve() keeps configuration`() {
        val parentNode = createMockNode()
        val routing = Routing(mock<Parcelable>())
        val unresolved = Unresolved(mock(), routing)
        val resolved = unresolved.resolve(defaultResolver, parentNode)
        assertEquals(routing, resolved.routing)
    }

    @Test
    fun `Unresolved resolve() keeps bundles`() {
        val parentNode = createMockNode()
        val bundles = List(NB_EXPECTED_NODES) { mock<Bundle>() }
        val unresolved = Unresolved<Parcelable>(mock(), mock(), bundles)
        val resolved = unresolved.resolve(defaultResolver, parentNode)
        assertEquals(bundles, resolved.bundles)
    }

    @Test
    fun `Unresolved resolve() resolves expected RoutingAction`() {
        val parentNode = createMockNode()
        val unresolved = Unresolved<Parcelable>(mock(), mock())
        val resolved = unresolved.resolve(defaultResolver, parentNode)
        assertEquals(defaultResolution, resolved.resolution)
    }

    @Test
    fun `Unresolved resolve() calls RoutingAction with parent as default anchor `() {
        val parentNode = createMockNode()
        verifyBuildNodesCalledCorrectly(
            defaultResolver,
            defaultResolution,
            parentNode,
            parentNode,
            NB_EXPECTED_NODES,
            emptyList<Bundle>()
        )
    }

    @Test
    fun `Unresolved resolve() calls RoutingAction with expected anchor`() {
        val parentNode = createMockNode()
        verifyBuildNodesCalledCorrectly(
            resolverWithAnchor,
            routingActionWithAnchor,
            mockAnchor,
            parentNode,
            NB_EXPECTED_NODES,
            emptyList<Bundle>()
        )
    }

    @Test
    fun `Unresolved resolve() calls RoutingAction with proper Bundles if there's any`() {
        val parentNode = createMockNode()
        verifyBuildNodesCalledCorrectly(
            defaultResolver,
            defaultResolution,
            parentNode,
            parentNode,
            NB_EXPECTED_NODES,
            List(NB_EXPECTED_NODES) { mock<Bundle>() }
        )
    }


    private fun verifyBuildNodesCalledCorrectly(
        resolver: RoutingResolver<Parcelable>,
        resolution: Resolution,
        expectedParent: Node<*>,
        parentNode: Node<*>,
        nbExpectedNodes: Int,
        bundles: List<Bundle>
    ) {
        assert(bundles.isEmpty() || bundles.size == nbExpectedNodes) {
            "Expected empty list of bundles or exactly $nbExpectedNodes, actual: ${bundles.size}"
        }

        val unresolved = Unresolved<Parcelable>(mock(), mock(), bundles)
        val resolved = unresolved.resolve(resolver, parentNode)
        val expectedAncestryInfo = AncestryInfo.Child(expectedParent, resolved.routing)

        argumentCaptor<List<BuildContext>>().apply {
            verify(resolution).buildNodes(capture())
            val list = firstValue
            assertEquals(nbExpectedNodes, list.size)

            list.forEachIndexed { idx, buildContext ->
                assertEquals(expectedAncestryInfo, buildContext.ancestryInfo)
                assertEquals(ActivationMode.ATTACH_TO_PARENT, buildContext.activationMode)

                if (bundles.isNotEmpty()) {
                    assertEquals(bundles[idx], buildContext.savedInstanceState)
                }
            }
        }
    }

    @Test
    fun `Unresolved resolve() stores built Nodes returned by RoutingAction buildNodes()`() {
        val unresolved = Unresolved<Parcelable>(mock(), mock())
        val resolved = unresolved.resolve(defaultResolver, createMockNode())
        assertEquals(nodes, resolved.nodes)
    }

    @Test
    fun `Resolved sleep() keeps INACTIVE`() {
        val resolved = Resolved<Parcelable>(INACTIVE, mock(), mock(), mock(), mock())
        val afterInvocation = resolved.sleep()
        assertEquals(INACTIVE, afterInvocation.activationState)
    }

    @Test
    fun `Resolved sleep() keeps SLEEPING`() {
        val resolved = Resolved<Parcelable>(SLEEPING, mock(), mock(), mock(), mock())
        val afterInvocation = resolved.sleep()
        assertEquals(SLEEPING, afterInvocation.activationState)
    }

    @Test
    fun `Resolved sleep() changes ACTIVE » SLEEPING`() {
        val resolved = Resolved<Parcelable>(ACTIVE, mock(), mock(), mock(), mock())
        val afterInvocation = resolved.sleep()
        assertEquals(SLEEPING, afterInvocation.activationState)
    }

    @Test
    fun `Resolved wakeUp() keeps INACTIVE`() {
        val resolved = Resolved<Parcelable>(INACTIVE, mock(), mock(), mock(), mock())
        val afterInvocation = resolved.wakeUp()
        assertEquals(INACTIVE, afterInvocation.activationState)
    }

    @Test
    fun `Resolved wakeUp() changes SLEEPING » ACTIVE`() {
        val resolved = Resolved<Parcelable>(SLEEPING, mock(), mock(), mock(), mock())
        val afterInvocation = resolved.wakeUp()
        assertEquals(ACTIVE, afterInvocation.activationState)
    }

    @Test
    fun `Resolved wakeUp() keeps ACTIVE`() {
        val resolved = Resolved<Parcelable>(ACTIVE, mock(), mock(), mock(), mock())
        val afterInvocation = resolved.wakeUp()
        assertEquals(ACTIVE, afterInvocation.activationState)
    }

    @Test
    fun `Resolved shrink() keeps INACTIVE`() {
        val resolved = Resolved<Parcelable>(INACTIVE, mock(), mock(), mock(), mock())
        val unresolved = resolved.shrink()
        assertEquals(INACTIVE, unresolved.activationState)
    }

    @Test
    fun `Resolved shrink() keeps SLEEPING`() {
        val resolved = Resolved<Parcelable>(SLEEPING, mock(), mock(), mock(), mock())
        val unresolved = resolved.shrink()
        assertEquals(SLEEPING, unresolved.activationState)
    }

    @Test
    fun `Resolved shrink() changes ACTIVE » SLEEPING`() {
        val resolved = Resolved<Parcelable>(ACTIVE, mock(), mock(), mock(), mock())
        val unresolved = resolved.shrink()
        assertEquals(SLEEPING, unresolved.activationState)
    }

    @Test
    fun `Resolved shrink() keeps configuration`() {
        val routing = Routing(mock<Parcelable>())
        val resolved = Resolved<Parcelable>(ACTIVE, routing, mock(), mock(), mock())
        val unresolved = resolved.shrink()
        assertEquals(routing, unresolved.routing)
    }

    @Test
    fun `Resolved shrink() keeps bundles`() {
        val bundles = mock<List<Bundle>>()
        val resolved = Resolved<Parcelable>(ACTIVE, mock(), bundles, mock(), mock())
        val unresolved = resolved.shrink()
        assertEquals(bundles, unresolved.bundles)
    }

    @Test
    fun `Resolved saveInstanceState() sets correct bundles`() {
        val bundles = mock<List<Bundle>>()
        val resolved = Resolved<Parcelable>(ACTIVE, mock(), bundles, mock(), nodes)
        val afterInvocation = resolved.saveInstanceState()

        afterInvocation.bundles.forEachIndexed { i, bundle ->
            verify(nodes[i]).onSaveInstanceState(bundle)
        }
    }
}
