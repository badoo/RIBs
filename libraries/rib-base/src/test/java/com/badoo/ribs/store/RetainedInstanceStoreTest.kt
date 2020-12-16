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

}
