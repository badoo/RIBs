package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.SystemAware
import org.mockito.kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginSystemAwareTest : NodePluginTest() {

    @Test
    fun `SystemAware plugins receive onLowMemory()`() {
        val (node, plugins) = testPlugins<SystemAware>()

        node.onLowMemory()

        plugins.forEach {
            verify(it).onLowMemory()
        }
    }

}
