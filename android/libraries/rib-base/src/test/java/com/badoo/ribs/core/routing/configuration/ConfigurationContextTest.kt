package com.badoo.ribs.core.routing.configuration

import android.os.Bundle
import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.ACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Unresolved
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class ConfigurationContextTest {

    private val nodes: List<Node<*>> = listOf(mock(), mock())
    private val nodeDescriptors: List<Node.Descriptor> = nodes.map { Node.Descriptor(it, mock()) }
    private val routingAction = mock<RoutingAction<*>> {
        on { buildNodes(any(), anyOrNull()) } doReturn nodeDescriptors
    }
    private val resolver = mock<(Parcelable) -> RoutingAction<*>> {
        on { invoke(any()) } doReturn routingAction
    }

    private val mockAnchor: Node<*> = mock()
    private val routingActionWithAnchor = mock<RoutingAction<*>> {
        on { buildNodes(any(), anyOrNull()) } doReturn nodeDescriptors
        on { anchor() } doReturn mockAnchor
    }
    private val resolverWithAnchor = mock<(Parcelable) -> RoutingAction<*>> {
        on { invoke(any()) } doReturn routingActionWithAnchor
    }

    private val onResolution = { resolved: Resolved<Parcelable> ->
        resolved
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
        val unresolved = Unresolved<Parcelable>(INACTIVE, mock())
        val resolved = unresolved.resolve(resolver, mock(), onResolution)
        assertEquals(INACTIVE, resolved.activationState)
    }

    @Test
    fun `Unresolved resolve() keeps SLEEPING`() {
        val unresolved = Unresolved<Parcelable>(SLEEPING, mock())
        val resolved = unresolved.resolve(resolver, mock(), onResolution)
        assertEquals(SLEEPING, resolved.activationState)
    }

    @Test
    fun `Unresolved resolve() keeps configuration`() {
        val configuration = mock<Parcelable>()
        val unresolved = Unresolved(mock(), configuration)
        val resolved = unresolved.resolve(resolver, mock(), onResolution)
        assertEquals(configuration, resolved.configuration)
    }

    @Test
    fun `Unresolved resolve() keeps bundles`() {
        val bundles = listOf(mock<Bundle>())
        val unresolved = Unresolved<Parcelable>(mock(), mock(), bundles)
        val resolved = unresolved.resolve(resolver, mock(), onResolution)
        assertEquals(bundles, resolved.bundles)
    }

    @Test
    fun `Unresolved resolve() resolves expected RoutingAction`() {
        val unresolved = Unresolved<Parcelable>(mock(), mock())
        val resolved = unresolved.resolve(resolver, mock(), onResolution)
        assertEquals(routingAction, resolved.routingAction)
    }

    @Test
    fun `Unresolved resolve() builds expected Nodes`() {
        val unresolved = Unresolved<Parcelable>(mock(), mock())
        val resolved = unresolved.resolve(resolver, mock(), onResolution)
        assertEquals(nodeDescriptors, resolved.nodes)
    }

    @Test
    fun `Unresolved resolve() sets AncestryInfo on built Nodes with parent as default anchor `() {
        val configuration = mock<Parcelable>()
        val unresolved = Unresolved(mock(), configuration)
        val targetResolver = resolver
        val parentNode = mock<Node<*>>()
        val resolved = unresolved.resolve(targetResolver, parentNode, onResolution)
        resolved.nodes.forEach {
            val expected = AncestryInfo.Child(
                parentNode,
                configuration
            )

            assertEquals(expected, it.node.ancestryInfo)
        }
    }

    @Test
    fun `Unresolved resolve() sets AncestryInfo on built Nodes with expected anchor `() {
        val configuration = mock<Parcelable>()
        val unresolved = Unresolved(mock(), configuration)
        val targetResolver = resolverWithAnchor
        val resolved = unresolved.resolve(targetResolver, mock(), onResolution)
        resolved.nodes.forEach {
            val expected = AncestryInfo.Child(
                mockAnchor,
                configuration
            )

            assertEquals(expected, it.node.ancestryInfo)
        }
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
        val configuration = mock<Parcelable>()
        val resolved = Resolved<Parcelable>(ACTIVE, configuration, mock(), mock(), mock())
        val unresolved = resolved.shrink()
        assertEquals(configuration, unresolved.configuration)
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
        val resolved = Resolved<Parcelable>(ACTIVE, mock(), bundles, mock(), nodeDescriptors)
        val afterInvocation = resolved.saveInstanceState()

        afterInvocation.bundles.forEachIndexed { i, bundle ->
            verify(nodes[i]).onSaveInstanceState(bundle)
        }
    }
}
