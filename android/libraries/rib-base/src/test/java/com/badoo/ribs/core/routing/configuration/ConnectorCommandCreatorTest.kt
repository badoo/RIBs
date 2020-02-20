package com.badoo.ribs.core.routing.configuration

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.C4
import com.badoo.ribs.core.helper.TestRouter.Configuration.C5
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Content
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Overlay
import com.badoo.ribs.core.routing.configuration.feature.BackStackElement
import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectorCommandCreatorTest {

    @Test
    fun `Content -- () » ()`() {
        val oldStack = backStack()
        val newStack = backStack()
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = emptyList<ConfigurationCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1) » (C1)`() {
        val oldStack = backStack(C1)
        val newStack = backStack(C1)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = emptyList<ConfigurationCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- () » (C1)`() {
        val oldStack = backStack()
        val newStack = backStack(C1)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Add(Content(0, C1),  C1),
            Activate(Content(0, C1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1) » ()`() {
        val oldStack = backStack(C1)
        val newStack = backStack()
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(0, C1)),
            Remove(Content(0, C1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1) » (C1, C2)`() {
        val oldStack = backStack(C1)
        val newStack = backStack(C1, C2)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(0, C1)),
            Add(Content(1, C2),  C2),
            Activate(Content(1, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » ()`() {
        val oldStack = backStack(C1, C2)
        val newStack = backStack()
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1, C2)),
            Remove(Content(1, C2)),
            Remove(Content(0, C1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C1)`() {
        val oldStack = backStack(C1, C2)
        val newStack = backStack(C1)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1, C2)),
            Remove(Content(1, C2)),
            Activate(Content(0, C1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C2)`() {
        val oldStack = backStack(C1, C2)
        val newStack = backStack(C2)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1, C2)),
            Remove(Content(1, C2)),
            Remove(Content(0, C1)),
            Add(Content(0, C2),  C2),
            Activate(Content(0, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C1, C2)`() {
        val oldStack = backStack(C1, C2)
        val newStack = backStack(C1, C2)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = emptyList<ConfigurationCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C1, C3)`() {
        val oldStack = backStack(C1, C2)
        val newStack = backStack(C1, C3)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1, C2)),
            Remove(Content(1, C2)),
            Add(Content(1, C3),  C3),
            Activate(Content(1, C3))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2) » (C1, C2, C3)`() {
        val oldStack = backStack(C1, C2)
        val newStack = backStack(C1, C2, C3)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1, C2)),
            Add(Content(2, C3),  C3),
            Activate(Content(2, C3))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1, C2)`() {
        val oldStack = backStack(C1, C2, C3)
        val newStack = backStack(C1, C2)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2, C3)),
            Remove(Content(2, C3)),
            Activate(Content(1, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1)`() {
        val oldStack = backStack(C1, C2, C3)
        val newStack = backStack(C1)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2, C3)),
            Remove(Content(2, C3)),
            Remove(Content(1, C2)),
            Activate(Content(0, C1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » ()`() {
        val oldStack = backStack(C1, C2, C3)
        val newStack = backStack()
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2, C3)),
            Remove(Content(2, C3)),
            Remove(Content(1, C2)),
            Remove(Content(0, C1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C5)`() {
        val oldStack = backStack(C1, C2, C3)
        val newStack = backStack(C5)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2, C3)),
            Remove(Content(2, C3)),
            Remove(Content(1, C2)),
            Remove(Content(0, C1)),
            Add(Content(0, C5),  C5),
            Activate(Content(0, C5))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1, C2, C4)`() {
        val oldStack = backStack(C1, C2, C3)
        val newStack = backStack(C1, C2, C4)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2, C3)),
            Remove(Content(2, C3)),
            Add(Content(2, C4),  C4),
            Activate(Content(2, C4))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Content -- (C1, C2, C3) » (C1, C4, C5)`() {
        val oldStack = backStack(C1, C2, C3)
        val newStack = backStack(C1, C4, C5)
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2, C3)),
            Remove(Content(2, C3)),
            Remove(Content(1, C2)),
            Add(Content(1, C4),  C4),
            Add(Content(2, C5),  C5),
            Activate(Content(2, C5))
        )
        assertEquals(expected, actual)
    }


    @Test
    fun `Overlays -- (C1, C2, C3) » (C1, C2, C3 {O1}) -- Add single overlay on last element with no overlays`() {
        val oldStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf()
        )
        val newStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1)
        )
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Add(
                Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1)),
                Configuration.O1
            ),
            Activate(Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1)))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3) » (C1, C2, C3 {O1, O2}) -- Add a second overlay on last element with a single overlay`() {
        val oldStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf()
        )
        val newStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1, Configuration.O2)
        )
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Add(
                Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1)),
                Configuration.O1
            ),
            Activate(Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1))),
            Add(
                Overlay(Overlay.Key(Content(2, C3), 1 ,Configuration.O2)),
                Configuration.O2
            ),
            Activate(Overlay(Overlay.Key(Content(2, C3), 1 ,Configuration.O2)))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1}) » (C1, C2, C3 {O1, O2}) -- Add multiple overlays on last element with no overlays`() {
        val oldStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1)
        )
        val newStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1, Configuration.O2)
        )
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Add(
                Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2)),
                Configuration.O2
            ),
            Activate(Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2)))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2, C3 {O1}) -- Remove single overlay on last element with multiple overlays`() {
        val oldStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1, Configuration.O2)
        )
        val newStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1)
        )
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2))),
            Remove(Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2)))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2, C3) -- Remove all overlays on last element with multiple overlays`() {
        val oldStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1, Configuration.O2)
        )
        val newStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf()
        )
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2))),
            Remove(Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2))),
            Deactivate(Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1))),
            Remove(Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1)))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2, C3 {O1, O2}) » (C1, C2) -- Remove last back stack element with multiple overlays`() {
        val oldStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(),
            C3 to listOf(Configuration.O1, Configuration.O2)
        )
        val newStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf()
        )
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2))),
            Deactivate(Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1))),
            Deactivate(Content(2, C3)),
            Remove(Overlay(Overlay.Key(Content(2, C3), 1, Configuration.O2))),
            Remove(Overlay(Overlay.Key(Content(2, C3), 0, Configuration.O1))),
            Remove(Content(2, C3)),
            Activate(Content(1, C2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `Overlays -- (C1, C2 {O1, O2}, C3) » (C1, C2 {O1, O2}) -- Going back to previous back stack element with multiple overlays`() {
        val oldStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(Configuration.O1, Configuration.O2),
            C3 to listOf()
        )
        val newStack = backStackWithOverlays(
            C1 to listOf(),
            C2 to listOf(Configuration.O1, Configuration.O2)
        )
        val actual = ConfigurationCommandCreator.diff(oldStack, newStack)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2, C3)),
            Remove(Content(2, C3)),
            Activate(Content(1, C2)),
            Activate(Overlay(Overlay.Key(Content(1, C2), 0, Configuration.O1))),
            Activate(Overlay(Overlay.Key(Content(1, C2), 1, Configuration.O2)))
        )
        assertEquals(expected, actual)
    }

    private fun backStack(vararg configurations: Configuration): List<BackStackElement<Configuration>> =
        configurations.map { BackStackElement(it) }

    private fun backStackWithOverlays(vararg configurations: Pair<Configuration, List<Configuration>>): List<BackStackElement<Configuration>> =
        configurations.map { BackStackElement(it.first, it.second) }
}
