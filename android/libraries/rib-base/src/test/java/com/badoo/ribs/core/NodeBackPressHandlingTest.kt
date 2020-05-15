package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.PriorityBackPressHandler
import com.badoo.ribs.core.plugin.SubtreeBackPressHandler
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodeBackPressHandlingTest : NodePluginTest() {

    @Test
    fun `BackPressHandler plugin receives handleBackPressBeforeDownstream()`() {
        val subtreeHandlers = List<SubtreeBackPressHandler>(3) { mock() }
        val priorityHandlers = List<PriorityBackPressHandler>(3) { mock() }
        val simpleHandlers = List<BackPressHandler>(3) { mock() }

        val node = createNode(plugins = subtreeHandlers + priorityHandlers + simpleHandlers)
        val activeChild = createChildNode(node).also { it.makeActiveBackPressHandler(true) }
        val inactiveChild = createChildNode(node)

        node.handleBackPress()
    }

//    @Test
//    fun `BackPressHandler plugins receive handleBackPressBeforeDownstream() only until one of them handles it`() {
//        val plugins: List<BackPressHandler> = List(5) { i -> mock<BackPressHandler> {
//            on { handleBackPressBeforeDownstream() } doReturn (i == 2)
//        }}
//
//        val node = createNode(plugins = plugins)
//
//        node.handleBackPress()
//        verify(plugins[0]).handleBackPressBeforeDownstream()
//        verify(plugins[1]).handleBackPressBeforeDownstream()
//        verify(plugins[2]).handleBackPressBeforeDownstream()
//        verify(plugins[3], never()).handleBackPressBeforeDownstream()
//        verify(plugins[4], never()).handleBackPressBeforeDownstream()
//    }
//
//    @Test
//    fun `BackPressHandler plugins don't receive handleBackPressAfterDownstream() if any one of them handled it in handleBackPressBeforeDownstream()`() {
//        val plugins: List<BackPressHandler> = List(5) { i -> mock<BackPressHandler> {
//            on { handleBackPressBeforeDownstream() } doReturn (i == 2)
//        }}
//
//        val node = createNode(plugins = plugins)
//
//        node.handleBackPress()
//        verify(plugins[0], never()).handleBackPressAfterDownstream()
//        verify(plugins[1], never()).handleBackPressAfterDownstream()
//        verify(plugins[2], never()).handleBackPressAfterDownstream()
//        verify(plugins[3], never()).handleBackPressAfterDownstream()
//        verify(plugins[4], never()).handleBackPressAfterDownstream()
//    }
//
//    @Test
//    fun `BackPressHandler plugins don't receive handleBackPressAfterDownstream() if subtree handled it`() {
//        val plugins: List<BackPressHandler> = List(5) { i -> mock<BackPressHandler> {
//            on { handleBackPressBeforeDownstream() } doReturn false
//        }}
//
//        val node = createNode(plugins = plugins)
//        val child = createChildNode(node)
//        child.makeActiveBackPressHandler(true) // TODO child related scenarios belong to NodeTest, add them there
//        node.attachChildNode(child)
//
//        node.handleBackPress()
//        verify(plugins[0], never()).handleBackPressAfterDownstream()
//        verify(plugins[1], never()).handleBackPressAfterDownstream()
//        verify(plugins[2], never()).handleBackPressAfterDownstream()
//        verify(plugins[3], never()).handleBackPressAfterDownstream()
//        verify(plugins[4], never()).handleBackPressAfterDownstream()
//    }
//
//    @Test
//    fun `BackPressHandler plugins do receive handleBackPressAfterDownstream() if subtree didn't handle it`() {
//        val plugins: List<BackPressHandler> = List(5) { i -> mock<BackPressHandler> {
//            on { handleBackPressBeforeDownstream() } doReturn false
//        }}
//
//        val node = createNode(plugins = plugins)
//        val child = createChildNode(node)
//        child.makeActiveBackPressHandler(false)
//        node.attachChildNode(child)
//
//        node.handleBackPress()
//        verify(plugins[0]).handleBackPressAfterDownstream()
//        verify(plugins[1]).handleBackPressAfterDownstream()
//        verify(plugins[2]).handleBackPressAfterDownstream()
//        verify(plugins[3]).handleBackPressAfterDownstream()
//        verify(plugins[4]).handleBackPressAfterDownstream()
//    }
//
//    @Test
//    fun `BackPressHandler plugins receive handleBackPressAfterDownstream() only until one of them handles it`() {
//        val plugins: List<BackPressHandler> = List(5) { i -> mock<BackPressHandler> {
//            on { handleBackPressBeforeDownstream() } doReturn false
//            on { handleBackPressAfterDownstream() } doReturn (i == 2)
//        }}
//
//        val node = createNode(plugins = plugins)
//        val child = createChildNode(node)
//        child.makeActiveBackPressHandler(false)
//        node.attachChildNode(child)
//
//        node.handleBackPress()
//        verify(plugins[0]).handleBackPressAfterDownstream()
//        verify(plugins[1]).handleBackPressAfterDownstream()
//        verify(plugins[2]).handleBackPressAfterDownstream()
//        verify(plugins[3], never()).handleBackPressAfterDownstream()
//        verify(plugins[4], never()).handleBackPressAfterDownstream()
//    }
}
