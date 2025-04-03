package com.badoo.ribs.routing.state

import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.C4
import com.badoo.ribs.core.helper.TestRouter.Configuration.C5
import com.badoo.ribs.core.helper.TestRouter.Configuration.O1
import com.badoo.ribs.core.helper.TestRouter.Configuration.O2
import com.badoo.ribs.core.helper.TestRouter.Configuration.O3
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.history.RoutingHistoryDiffer
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.contentIdForPosition
import com.badoo.ribs.routing.source.backstack.operation.newRoot
import com.badoo.ribs.routing.source.backstack.operation.pop
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.routing.source.backstack.operation.singleTop
import com.badoo.ribs.routing.source.backstack.overlayIdForPosition
import com.badoo.ribs.routing.state.changeset.RoutingCommand
import com.badoo.ribs.routing.state.changeset.RoutingCommand.Activate
import com.badoo.ribs.routing.state.changeset.RoutingCommand.Add
import com.badoo.ribs.routing.state.changeset.RoutingCommand.Deactivate
import com.badoo.ribs.routing.state.changeset.RoutingCommand.Remove
import com.badoo.ribs.test.emptyBuildParams
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.badoo.ribs.core.helper.TestRouter.Configuration as C

class BackStackDiffingIntegrationTest {

    private lateinit var backStack: BackStack<C>

    @Before
    fun setUp() {
        backStack = BackStack(
            initialConfiguration = C1,
            buildParams = emptyBuildParams()
        )
    }

    fun routing(position: Int, content: C): Routing<C> =
        Routing(
            configuration = content,
            identifier = backStack.state.contentIdForPosition(position, content)
        )

    fun overlay(position: Int, content: C, overlayIndex: Int, overlay: C): Routing<C> =
        Routing(
            configuration = overlay,
            identifier = backStack.state.overlayIdForPosition(
                position,
                content,
                overlayIndex,
                overlay
            )
        )

    @Test
    fun `Content -- (C1) » (C1)`() {
        val oldStack = backStack.state
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = emptySet<RoutingCommand<C>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1) » (C1, C2)`() {
        val oldStack = backStack.state
        backStack.push(C2)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(0, C1)),
            Add(routing(1, C2)),
            Activate(routing(1, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C1)`() {
        backStack.push(C2)
        val oldStack = backStack.state
        backStack.popBackStack()
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(1, C2)),
            Remove(routing(1, C2)),
            Activate(routing(0, C1))
        )
        assertEquals(expected, actual)
    }


    @Test
    fun `Content -- (C1, C2) » (C2)`() {
        backStack.push(C2)
        val oldStack = backStack.state
        backStack.newRoot(C2)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(1, C2)),
            Remove(routing(1, C2)),
            Remove(routing(0, C1)),
            Add(routing(0, C2)),
            Activate(routing(0, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C1, C3)`() {
        backStack.push(C2)
        val oldStack = backStack.state
        backStack.pop()
        backStack.push(C3)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(1, C2)),
            Remove(routing(1, C2)),
            Add(routing(1, C3)),
            Activate(routing(1, C3))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C1, C2, C3)`() {
        backStack.push(C2)
        val oldStack = backStack.state
        backStack.push(C3)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(1, C2)),
            Add(routing(2, C3)),
            Activate(routing(2, C3))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1, C2)`() {
        backStack.push(C2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.pop()
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(2, C3)),
            Remove(routing(2, C3)),
            Activate(routing(1, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1)`() {
        backStack.push(C2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.pop()
        backStack.pop()
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(2, C3)),
            Remove(routing(2, C3)),
            Remove(routing(1, C2)),
            Activate(routing(0, C1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C5)`() {
        backStack.push(C2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.newRoot(C5)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(2, C3)),
            Remove(routing(2, C3)),
            Remove(routing(1, C2)),
            Remove(routing(0, C1)),
            Add(routing(0, C5)),
            Activate(routing(0, C5))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1, C2, C4)`() {
        backStack.push(C2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.replace(C4)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(2, C3)),
            Remove(routing(2, C3)),
            Add(routing(2, C4)),
            Activate(routing(2, C4))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1, C4, C5)`() {
        backStack.push(C2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.pop()
        backStack.replace(C4)
        backStack.push(C5)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(2, C3)),
            Remove(routing(2, C3)),
            Remove(routing(1, C2)),
            Add(routing(1, C4)),
            Add(routing(2, C5)),
            Activate(routing(2, C5))
        )
        assertEquals(expected, actual)
    }


    @Test
    fun `Overlays -- (C1, C2, C3) » (C1, C2, C3 {O1}) -- Add single overlay on last element with no overlays`() {
        backStack.push(C2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.pushOverlay(O1)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Add(overlay(2, C3, 0, O1)),
            Activate(overlay(2, C3, 0, O1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1}) » (C1, C2, C3 {O1, O2}) -- Add a second overlay on last element with a single overlay`() {
        backStack.push(C2)
        backStack.push(C3)
        backStack.pushOverlay(O1)
        val oldStack = backStack.state
        backStack.pushOverlay(O2)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Add(overlay(2, C3, 1, O2)),
            Activate(overlay(2, C3, 1, O2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3) » (C1, C2, C3 {O1, O2}) -- Add multiple overlays on last element with no overlays`() {
        backStack.push(C2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.pushOverlay(O1)
        backStack.pushOverlay(O2)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Add(overlay(2, C3, 0, O1)),
            Add(overlay(2, C3, 1, O2)),
            Activate(overlay(2, C3, 0, O1)),
            Activate(overlay(2, C3, 1, O2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2, C3 {O1}) -- Remove single overlay on last element with multiple overlays`() {
        backStack.push(C2)
        backStack.push(C3)
        backStack.pushOverlay(O1)
        backStack.pushOverlay(O2)
        val oldStack = backStack.state
        backStack.pop()
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(overlay(2, C3, 1, O2)),
            Remove(overlay(2, C3, 1, O2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2, C3) -- Remove all overlays on last element with multiple overlays`() {
        backStack.push(C2)
        backStack.push(C3)
        backStack.pushOverlay(O1)
        backStack.pushOverlay(O2)
        val oldStack = backStack.state
        backStack.pop()
        backStack.pop()
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(overlay(2, C3, 0, O1)),
            Deactivate(overlay(2, C3, 1, O2)),
            Remove(overlay(2, C3, 0, O1)),
            Remove(overlay(2, C3, 1, O2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2) -- Remove last back stack element with multiple overlays`() {
        backStack.push(C2)
        backStack.push(C3)
        backStack.pushOverlay(O1)
        backStack.pushOverlay(O2)
        val oldStack = backStack.state
        backStack.singleTop(C2)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(overlay(2, C3, 0, O1)),
            Deactivate(overlay(2, C3, 1, O2)),
            Deactivate(routing(2, C3)),
            Remove(overlay(2, C3, 0, O1)),
            Remove(overlay(2, C3, 1, O2)),
            Remove(routing(2, C3)),
            Activate(routing(1, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2 {O1, O2}, C3) » (C1, C2 {O1, O2}) -- Going back to previous back stack element with multiple overlays`() {
        backStack.push(C2)
        backStack.pushOverlay(O1)
        backStack.pushOverlay(O2)
        backStack.push(C3)
        val oldStack = backStack.state
        backStack.singleTop(C2)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(routing(2, C3)),
            Remove(routing(2, C3)),
            Activate(routing(1, C2)),
            Activate(overlay(1, C2, 0, O1)),
            Activate(overlay(1, C2, 1, O2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2 {O1, O2}, C3 {O1, O2}) » (C1, C2 {O1, O2, O3}) -- Going back to previous back stack element with multiple overlays`() {
        backStack.push(C2)
        backStack.pushOverlay(O1)
        backStack.pushOverlay(O2)
        backStack.push(C3)
        backStack.pushOverlay(O1)
        backStack.pushOverlay(O2)
        val oldStack = backStack.state
        backStack.singleTop(C2)
        backStack.pushOverlay(O3)
        val newStack = backStack.state
        val actual = RoutingHistoryDiffer.diff(oldStack, newStack)
        val expected = setOf(
            Deactivate(overlay(2, C3, 0, O1)),
            Deactivate(overlay(2, C3, 1, O2)),
            Deactivate(routing(2, C3)),
            Remove(overlay(2, C3, 0, O1)),
            Remove(overlay(2, C3, 1, O2)),
            Remove(routing(2, C3)),
            Activate(routing(1, C2)),
            Activate(overlay(1, C2, 0, O1)),
            Activate(overlay(1, C2, 1, O2)),
            Add(overlay(1, C2, 2, O3)),
            Activate(overlay(1, C2, 2, O3))
        )
        assertEquals(expected, actual)
    }
}
