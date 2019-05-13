package com.badoo.ribs.core.routing.backstack

import com.badoo.ribs.core.helper.TestRouter.Configuration
import com.badoo.ribs.core.helper.TestRouter.Configuration.C1
import com.badoo.ribs.core.helper.TestRouter.Configuration.C2
import com.badoo.ribs.core.helper.TestRouter.Configuration.C3
import com.badoo.ribs.core.helper.TestRouter.Configuration.C4
import com.badoo.ribs.core.helper.TestRouter.Configuration.C5
import com.badoo.ribs.core.routing.backstack.ConnectorCommand.Add
import com.badoo.ribs.core.routing.backstack.ConnectorCommand.MakeActive
import com.badoo.ribs.core.routing.backstack.ConnectorCommand.MakePassive
import com.badoo.ribs.core.routing.backstack.ConnectorCommand.Remove
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConnectorCommandCreatorTest {

    private lateinit var obj: ConnectorCommandCreator<Configuration>

    @Before
    fun setUp() {
        obj = ConnectorCommandCreator()
    }

    @Test
    fun `() » ()`() {
        val oldState = state(emptyList())
        val newState = state(emptyList())
        val actual = obj.invoke(oldState, newState)
        val expected = emptyList<ConnectorCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1) » (C1)`() {
        val oldState = state(listOf(C1))
        val newState = state(listOf(C1))
        val actual = obj.invoke(oldState, newState)
        val expected = emptyList<ConnectorCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `() » (C1)`() {
        val oldState = state(emptyList())
        val newState = state(listOf(C1))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            Add(0, C1),
            MakeActive(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1) » ()`() {
        val oldState = state(listOf(C1))
        val newState = state(emptyList())
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(0),
            Remove(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1) » (C1, C2)`() {
        val oldState = state(listOf(C1))
        val newState = state(listOf(C1, C2))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(0),
            Add(1, C2),
            MakeActive(1)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » ()`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(emptyList())
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(1),
            Remove(1),
            Remove(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(1),
            Remove(1),
            MakeActive(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C2)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C2))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(1),
            Remove(1),
            Remove(0),
            Add(0, C2),
            MakeActive(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1, C2)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1, C2))
        val actual = obj.invoke(oldState, newState)
        val expected = emptyList<ConnectorCommand<Configuration>>()
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1, C3)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1, C3))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(1),
            Remove(1),
            Add(1, C3),
            MakeActive(1)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2) » (C1, C2, C3)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1, C2, C3))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(1),
            Add(2, C3),
            MakeActive(2)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1, C2)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C1, C2))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(2),
            Remove(2),
            MakeActive(1)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C1))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(2),
            Remove(2),
            Remove(1),
            MakeActive(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » ()`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf())
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(2),
            Remove(2),
            Remove(1),
            Remove(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C5)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C5))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(2),
            Remove(2),
            Remove(1),
            Remove(0),
            Add(0, C5),
            MakeActive(0)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1, C2, C4)`() {
        val oldState = state(listOf(C1, C2, C3))
        val newState = state(listOf(C1, C2, C4))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(2),
            Remove(2),
            Add(2, C4),
            MakeActive(2)
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `(C1, C2, C3) » (C1, C4, C5)`() {
        val oldState = state(listOf(C1, C2))
        val newState = state(listOf(C1, C4, C5))
        val actual = obj.invoke(oldState, newState)
        val expected = listOf<ConnectorCommand<Configuration>>(
            MakePassive(1),
            Remove(1),
            Add(1, C4),
            Add(2, C5),
            MakeActive(2)
        )
        assertEquals(expected, actual)
    }

    private fun state(list: List<Configuration>) =
        BackStackManager.State(backStack = list)
}
