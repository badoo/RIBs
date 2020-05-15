package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.ViewLifecycleAware
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginViewLifecycleAwareTest : NodePluginTest() {

    @Test
    fun `ViewLifecycleAware plugins receive onAttachToView()`() {
        val (node, plugins) = testPlugins<ViewLifecycleAware>()

        node.attachToView(parentViewGroup)

        plugins.forEach {
            verify(it).onAttachToView(parentViewGroup)
        }
    }

    @Test
    fun `ViewLifecycleAware plugins receive onDetachFromView()`() {
        val (node, plugins) = testPlugins<ViewLifecycleAware>()

        node.attachToView(parentViewGroup)
        node.detachFromView()

        plugins.forEach {
            verify(it).onDetachFromView(parentViewGroup)
        }
    }
}
