package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.SubtreeViewChangeAware
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginSubtreeViewChangeAwareTest : NodePluginTest() {

    @Test
    fun `SubtreeViewChangeAware plugins receive onAttachChildView()`() {
        val (node, plugins) = testPlugins<SubtreeViewChangeAware>()
        val childNode = createChildNode(parent = node)

        node.onCreateView(parentView)
        node.onAttachToView()
        node.attachChildView(childNode)

        plugins.forEach {
            verify(it).onChildViewAttached(eq(childNode))
        }
    }

    @Test
    fun `SubtreeViewChangeAware plugins receive onDetachChildView()`() {
        val (node, plugins) = testPlugins<SubtreeViewChangeAware>()
        val childNode = createChildNode(parent = node)

        node.onCreateView(parentView)
        node.onAttachToView()
        childNode.onAttachToView()
        node.detachChildView(childNode)

        plugins.forEach {
            verify(it).onChildViewDetached(eq(childNode))
        }
    }
}
