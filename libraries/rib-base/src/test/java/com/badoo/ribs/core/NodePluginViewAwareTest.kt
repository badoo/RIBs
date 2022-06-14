package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.plugin.ViewAware
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginViewAwareTest : NodePluginTest() {

    @Test
    fun `ViewAware plugins receive onViewCreated()`() {
        val (node, plugins) = testPlugins<ViewAware<TestView>>()

        node.onCreateView(parentView)

        plugins.forEach {
            verify(it).onViewCreated(any(), eq(node.lifecycleManager.viewLifecycle!!.lifecycle))
        }
    }
}
