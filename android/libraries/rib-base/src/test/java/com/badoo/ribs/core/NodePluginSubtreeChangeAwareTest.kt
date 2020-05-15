package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginSubtreeChangeAwareTest : NodePluginTest() {

    @Test
    fun `SubtreeChangeAware plugins receive onAttachChildNode()`() {
        val (node, plugins) = testPlugins<SubtreeChangeAware>()
        val childNode = createChildNode(parent = node)

        node.attachChildNode(childNode)

        plugins.forEach {
            verify(it).onAttachChildNode(eq(childNode))
        }
    }

    @Test
    fun `SubtreeChangeAware plugins receive onDetachChildNode()`() {
        val (node, plugins) = testPlugins<SubtreeChangeAware>()
        val childNode = createChildNode(parent = node)

        node.detachChildNode(childNode)

        plugins.forEach {
            verify(it).onDetachChildNode(eq(childNode))
        }
    }
}
