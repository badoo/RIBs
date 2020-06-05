package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodeBackPressHandlingTest : NodePluginTest() {
    
    private lateinit var subtreeHandlers: List<SubtreeBackPressHandler>
    private lateinit var simpleHandlers: List<BackPressHandler>
    private lateinit var inactiveChild: TestNode
    private lateinit var activeChild1: TestNode
    private lateinit var activeChild2: TestNode
    private lateinit var activeChild3: TestNode

    fun scenario(
        subtreeHandlerReturnsTrueOnFirst: Int? = null,
        subtreeHandlerReturnsTrueOnFallback: Int? = null,
        simpleHandlerReturnsTrue: Int? = null
    ): Node<TestView> {
        subtreeHandlers = List<SubtreeBackPressHandler>(3) { i -> mock {
            on { handleBackPressFirst() } doReturn (subtreeHandlerReturnsTrueOnFirst == i)
            on { handleBackPressFallback() } doReturn (subtreeHandlerReturnsTrueOnFallback == i)
        }}
        simpleHandlers = List<BackPressHandler>(3) { i -> mock {
            on { handleBackPress() } doReturn (simpleHandlerReturnsTrue == i)
        }}

        val node = createNode(plugins = subtreeHandlers + simpleHandlers)
        inactiveChild = createChildNode(node)
        activeChild1 = createChildNode(node).also { it.makeActive(true) }
        activeChild2 = createChildNode(node).also { it.makeActive(true) }
        activeChild3 = createChildNode(node).also { it.makeActive(true) }
        node.attachChildNode(inactiveChild)
        node.attachChildNode(activeChild1)
        node.attachChildNode(activeChild2)
        node.attachChildNode(activeChild3)

        return node
    }

    /**
     * All scenarios test expected stage where propagation stops.
     * Each is expected to go one step further.
     * Inactive child nodes are never supposed to be asked.
     */

    @Test
    fun `Base scenario - All active back press handlers are invoked if none of them handle it`() {
        val node = scenario()
        node.handleBackPress()

        verify(subtreeHandlers[0]).handleBackPressFirst()
        verify(subtreeHandlers[1]).handleBackPressFirst()
        verify(subtreeHandlers[2]).handleBackPressFirst()
        assertFalse(inactiveChild.handleBackPressInvoked)
        assertTrue(activeChild1.handleBackPressInvoked)
        assertTrue(activeChild2.handleBackPressInvoked)
        assertTrue(activeChild3.handleBackPressInvoked)
        verify(simpleHandlers[0]).handleBackPress()
        verify(simpleHandlers[1]).handleBackPress()
        verify(simpleHandlers[2]).handleBackPress()
        verify(subtreeHandlers[0]).handleBackPressFallback()
        verify(subtreeHandlers[1]).handleBackPressFallback()
        verify(subtreeHandlers[2]).handleBackPressFallback()
    }

    @Test
    fun `Scenario 1 - Propagation stops after as expected after first handler returns true`() {
        val node = scenario(subtreeHandlerReturnsTrueOnFirst = 1)
        node.handleBackPress()

        verify(subtreeHandlers[0]).handleBackPressFirst()
        verify(subtreeHandlers[1]).handleBackPressFirst()
        verify(subtreeHandlers[2], never()).handleBackPressFirst()
        assertFalse(inactiveChild.handleBackPressInvoked)
        assertFalse(activeChild1.handleBackPressInvoked)
        assertFalse(activeChild2.handleBackPressInvoked)
        assertFalse(activeChild3.handleBackPressInvoked)
        verify(simpleHandlers[0], never()).handleBackPress()
        verify(simpleHandlers[1], never()).handleBackPress()
        verify(simpleHandlers[2], never()).handleBackPress()
        verify(subtreeHandlers[0], never()).handleBackPressFallback()
        verify(subtreeHandlers[1], never()).handleBackPressFallback()
        verify(subtreeHandlers[2], never()).handleBackPressFallback()
    }

    @Test
    fun `Scenario 2 - Propagation stops after as expected after first handler returns true`() {
        val node = scenario(simpleHandlerReturnsTrue = 1)
        node.handleBackPress()

        verify(subtreeHandlers[0]).handleBackPressFirst()
        verify(subtreeHandlers[1]).handleBackPressFirst()
        verify(subtreeHandlers[2]).handleBackPressFirst()
        assertFalse(inactiveChild.handleBackPressInvoked)
        assertTrue(activeChild1.handleBackPressInvoked)
        assertTrue(activeChild2.handleBackPressInvoked)
        assertTrue(activeChild3.handleBackPressInvoked)
        verify(simpleHandlers[0]).handleBackPress()
        verify(simpleHandlers[1]).handleBackPress()
        verify(simpleHandlers[2], never()).handleBackPress()
        verify(subtreeHandlers[0], never()).handleBackPressFallback()
        verify(subtreeHandlers[1], never()).handleBackPressFallback()
        verify(subtreeHandlers[2], never()).handleBackPressFallback()
    }

    @Test
    fun `Scenario 3 - Propagation stops after as expected after first handler returns true`() {
        val node = scenario()
        activeChild2.handleBackPress = true
        node.handleBackPress()

        verify(subtreeHandlers[0]).handleBackPressFirst()
        verify(subtreeHandlers[1]).handleBackPressFirst()
        verify(subtreeHandlers[2]).handleBackPressFirst()
        assertFalse(inactiveChild.handleBackPressInvoked)
        assertTrue(activeChild1.handleBackPressInvoked)
        assertTrue(activeChild2.handleBackPressInvoked)
        assertFalse(activeChild3.handleBackPressInvoked)
        verify(simpleHandlers[0], never()).handleBackPress()
        verify(simpleHandlers[1], never()).handleBackPress()
        verify(simpleHandlers[2], never()).handleBackPress()
        verify(subtreeHandlers[0], never()).handleBackPressFallback()
        verify(subtreeHandlers[1], never()).handleBackPressFallback()
        verify(subtreeHandlers[2], never()).handleBackPressFallback()
    }

    @Test
    fun `Scenario 4 - Propagation stops after as expected after first handler returns true`() {
        val node = scenario(subtreeHandlerReturnsTrueOnFallback = 1)
        node.handleBackPress()

        verify(subtreeHandlers[0]).handleBackPressFirst()
        verify(subtreeHandlers[1]).handleBackPressFirst()
        verify(subtreeHandlers[2]).handleBackPressFirst()
        assertFalse(inactiveChild.handleBackPressInvoked)
        assertTrue(activeChild1.handleBackPressInvoked)
        assertTrue(activeChild2.handleBackPressInvoked)
        assertTrue(activeChild3.handleBackPressInvoked)
        verify(simpleHandlers[0]).handleBackPress()
        verify(simpleHandlers[1]).handleBackPress()
        verify(simpleHandlers[2]).handleBackPress()
        verify(subtreeHandlers[0]).handleBackPressFallback()
        verify(subtreeHandlers[1]).handleBackPressFallback()
        verify(subtreeHandlers[2], never()).handleBackPressFallback()
    }
}
