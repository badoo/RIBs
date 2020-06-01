package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.NodeAware
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginNodeAwareTest : NodePluginTest() {

    @Test
    fun `NodeAware plugins immediately receive init()`() {
        val (node, plugins) = testPlugins<NodeAware>()

        plugins.forEach {
            verify(it).init(node)
        }
    }
}
