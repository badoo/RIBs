package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestBuilder
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginNodeLifecycleAwareTest : NodePluginTest() {

    @Test
    fun `NodeLifecycleAware plugins receive onBuild() when created`() {
        val plugins = List<NodeLifecycleAware>(3) { mock() }
        val builder = TestBuilder(plugins)

        builder.build(BuildContext.root(null))

        plugins.forEach {
            verify(it).onBuild()
        }
    }

    @Test
    fun `NodeLifecycleAware plugins receive onAttach()`() {
        val (node, plugins) = testPlugins<NodeLifecycleAware>()

        node.onCreate()

        plugins.forEach {
            verify(it).onCreate(eq(node.lifecycleManager.ribLifecycle.lifecycle))
        }
    }

    @Test
    fun `NodeLifecycleAware plugins receive onDetach()`() {
        val (node, plugins) = testPlugins<NodeLifecycleAware>()

        node.onDestroy(isRecreating = false)

        plugins.forEach {
            verify(it).onDestroy()
        }
    }
}
