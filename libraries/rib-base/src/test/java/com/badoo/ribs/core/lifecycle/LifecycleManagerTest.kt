package com.badoo.ribs.core.lifecycle

import androidx.lifecycle.Lifecycle.State.RESUMED
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.helper.testBuildParams
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.routing.Routing
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.CopyOnWriteArrayList

class LifecycleManagerTest {
    internal lateinit var lifecycleManager: LifecycleManager
    private val child1: Node<*> = TestNode()
    private val children = CopyOnWriteArrayList(listOf(child1))
    private val owner = mock<Node<*>> {
        on { children } doReturn children
    }

    @BeforeEach
    fun setUp() {
        lifecycleManager = LifecycleManager(owner)
    }

    @Test
    fun `Lifecycle is whole RIB lifecycle`() {
        val expected = lifecycleManager.ribLifecycle.lifecycle
        val actual = lifecycleManager.lifecycle
        assertEquals(expected, actual)
    }

    @Test
    fun `onAttachChild() implies child inherits external lifecycle`() {
        val expected = RESUMED
        lifecycleManager.externalLifecycle.currentState = expected
        lifecycleManager.onAttachChild(child1)

        val actual = child1.lifecycleManager.externalLifecycle.currentState
        assertEquals(expected, actual)
    }

    @Test
    fun `onAttachChild() lifecycle inheritance is recursive`() {
        val grandChildAncestryInfo = AncestryInfo.Child(child1, Routing(AnyConfiguration))
        val grandChild: Node<*> = TestNode(buildParams = testBuildParams(ancestryInfo = grandChildAncestryInfo))
        child1.attachChildNode(grandChild)

        val expected = RESUMED
        lifecycleManager.externalLifecycle.currentState = expected
        lifecycleManager.onAttachChild(child1)

        val actual = grandChild.lifecycleManager.externalLifecycle.currentState
        assertEquals(expected, actual)
    }
}
