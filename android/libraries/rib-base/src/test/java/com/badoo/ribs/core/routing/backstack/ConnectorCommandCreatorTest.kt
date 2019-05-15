package com.badoo.ribs.core.routing.backstack

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.C4
import com.badoo.ribs.core.helper.TestRouter.Configuration.C5
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Remove
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Content
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature
import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectorCommandCreatorTest {

    @Test
    fun `() » ()`() {
        val oldState = state(emptyList())
        val newState = state(emptyList())
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = emptyList<ConfigurationCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1) » (C1)`() {
        val oldState = state(listOf(C1))
        val newState = state(listOf(C1))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = emptyList<ConfigurationCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `() » (C1)`() {
        val oldState = state(emptyList())
        val newState = state(listOf(C1))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Add(Content(0), C1),
            Activate(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1) » ()`() {
        val oldState = state(listOf(C1))
        val newState = state(emptyList())
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(0)),
            Remove(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1) » (C1, C2)`() {
        val oldState = state(listOf(C1))
        val newState = state(listOf(C1, C2))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(0)),
            Add(Content(1), C2),
            Activate(Content(1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » ()`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(emptyList())
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1)),
            Remove(Content(1)),
            Remove(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1)),
            Remove(Content(1)),
            Activate(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C2)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C2))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1)),
            Remove(Content(1)),
            Remove(Content(0)),
            Add(Content(0), C2),
            Activate(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1, C2)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1, C2))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = emptyList<ConfigurationCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1, C3)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1, C3))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1)),
            Remove(Content(1)),
            Add(Content(1), C3),
            Activate(Content(1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1, C2, C3)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1, C2, C3))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1)),
            Add(Content(2), C3),
            Activate(Content(2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1, C2)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C1, C2))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2)),
            Remove(Content(2)),
            Activate(Content(1))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C1))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2)),
            Remove(Content(2)),
            Remove(Content(1)),
            Activate(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » ()`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf())
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2)),
            Remove(Content(2)),
            Remove(Content(1)),
            Remove(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C5)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C5))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2)),
            Remove(Content(2)),
            Remove(Content(1)),
            Remove(Content(0)),
            Add(Content(0), C5),
            Activate(Content(0))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1, C2, C4)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C1, C2, C4))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(2)),
            Remove(Content(2)),
            Add(Content(2), C4),
            Activate(Content(2))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1, C4, C5)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C1, C4, C5))
        val actual = ConfigurationCommandCreator.diff(oldState, newState)
        val expected = listOf<ConfigurationCommand<Configuration>>(
            Deactivate(Content(1)),
            Remove(Content(1)),
            Add(Content(1), C4),
            Add(Content(2), C5),
            Activate(Content(2))
        )
        assertEquals(expected, actual)
    }

    private fun state(list: List<Configuration>) =
        BackStackFeature.State(backStack = list)
}
