package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.plugin.ViewAware
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
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
