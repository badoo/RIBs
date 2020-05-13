package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.RibLifecycleAware
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginRibLifecycleAwareTest : NodePluginTest() {

    @Test
    fun `RibLifecycleAware plugins receive onAttach()`() {
        val (node, plugins) = testPlugins<RibLifecycleAware>()

        node.onAttach()

        plugins.forEach {
            verify(it).onAttach(eq(node.lifecycleManager.ribLifecycle.lifecycle))
        }
    }

    @Test
    fun `RibLifecycleAware plugins receive onDetach()`() {
        val (node, plugins) = testPlugins<RibLifecycleAware>()

        node.onDetach()

        plugins.forEach {
            verify(it).onDetach()
        }
    }

    @Test
    fun `RibLifecycleAware plugins receive onAttachToView()`() {
        val (node, plugins) = testPlugins<RibLifecycleAware>()

        node.attachToView(parentViewGroup)

        plugins.forEach {
            verify(it).onAttachToView(parentViewGroup)
        }
    }

    @Test
    fun `RibLifecycleAware plugins receive onDetachFromView()`() {
        val (node, plugins) = testPlugins<RibLifecycleAware>()

        node.attachToView(parentViewGroup)
        node.detachFromView()

        plugins.forEach {
            verify(it).onDetachFromView(parentViewGroup)
        }
    }
}
