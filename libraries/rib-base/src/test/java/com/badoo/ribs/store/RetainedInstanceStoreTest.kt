package com.badoo.ribs.store

import com.badoo.ribs.core.Rib
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class RetainedInstanceStoreTest {

    private val identifier = Rib.Identifier(UUID.randomUUID())

    @Test
    fun `Retrieve the object which was stored before`() {
        val obj = Any()
        RetainedInstanceStore.get(identifier) { obj }

        val retrieved = RetainedInstanceStore.get(identifier) { Any() }

        assertEquals(obj, retrieved)
    }

    @Test
    fun `The object should be disposed after removeAll`() {
        val obj = Any()
        var disposed = false
        RetainedInstanceStore.get(identifier, disposer = { disposed = true }) { obj }
        RetainedInstanceStore.removeAll(identifier)

        assertTrue(disposed)
    }

    @Test
    fun `Unrelated to the current RIB object should not be disposed after removeAll`() {
        val obj = Any()
        val otherIdentifier = Rib.Identifier(UUID.randomUUID())
        var disposed = false
        RetainedInstanceStore.get(identifier) { obj }
        RetainedInstanceStore.get(otherIdentifier, disposer = { disposed = true }) { obj }
        RetainedInstanceStore.removeAll(identifier)

        assertFalse(disposed)
    }

    @Test
    fun `GIVEN object stored before WHEN checking if same instance retained by store id with same owner THEN expect true`() {
        val obj = Any()
        RetainedInstanceStore.get(identifier) { obj }

        val isRetained = RetainedInstanceStore.isRetainedByOwner(identifier, obj)

        assertTrue(isRetained)
    }

    @Test
    fun `GIVEN object stored before WHEN checking if same instance retained by store id with different owner THEN expect false`() {
        val obj = Any()
        RetainedInstanceStore.get(identifier) { obj }

        val isRetained = RetainedInstanceStore.isRetainedByOwner(Rib.Identifier(UUID.randomUUID()), obj)

        assertFalse(isRetained)
    }

    @Test
    fun `GIVEN object stored before WHEN checking if different instance retained by store id with same owner THEN expect false`() {
        val obj = Any()
        val otherObj = Any()
        RetainedInstanceStore.get(identifier) { obj }

        val isRetained = RetainedInstanceStore.isRetainedByOwner(identifier, otherObj)

        assertFalse(isRetained)
    }

    @Test
    fun `GIVEN object stored before AND then removed WHEN checking if same instance retained by store id with same owner THEN expect false`() {
        val obj = Any()
        RetainedInstanceStore.get(identifier) { obj }
        RetainedInstanceStore.removeAll(identifier)

        val isRetained = RetainedInstanceStore.isRetainedByOwner(identifier, obj)

        assertFalse(isRetained)
    }

    @Test
    fun `GIVEN no object stored WHEN checking if instance retained by store id THEN expect false`() {
        val obj = Any()

        val isRetained = RetainedInstanceStore.isRetainedByOwner(identifier, obj)

        assertFalse(isRetained)
    }

    @Test
    fun `GIVEN object stored before WHEN checking if same instance retained THEN expect true`() {
        val obj = Any()
        RetainedInstanceStore.get(identifier) { obj }

        val isRetained = RetainedInstanceStore.isRetained(obj)

        assertTrue(isRetained)
    }

    @Test
    fun `GIVEN object stored before WHEN checking if different instance retained THEN expect false`() {
        val obj = Any()
        val otherObj = Any()
        RetainedInstanceStore.get(identifier) { obj }

        val isRetained = RetainedInstanceStore.isRetained(otherObj)

        assertFalse(isRetained)
    }

    @Test
    fun `GIVEN object stored before AND then removed WHEN checking if same instance retained THEN expect false`() {
        val obj = Any()
        RetainedInstanceStore.get(identifier) { obj }
        RetainedInstanceStore.removeAll(identifier)

        val isRetained = RetainedInstanceStore.isRetained(obj)

        assertFalse(isRetained)
    }

    @Test
    fun `GIVEN no object stored WHEN checking if instance retained THEN expect false`() {
        val obj = Any()

        val isRetained = RetainedInstanceStore.isRetained(obj)

        assertFalse(isRetained)
    }

}
