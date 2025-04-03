package com.badoo.ribs.core

import com.badoo.ribs.core.helper.TestBuilder
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginSubtreeChangeAwareTest : NodePluginTest() {

    @Test
    fun `SubtreeChangeAware plugins receive onChildCreated()`() {
        val (node, plugins) = testPlugins<SubtreeChangeAware>()

        val builder = TestBuilder()
        val buildContext = BuildContext(
            ancestryInfo = AncestryInfo.Child(
                anchor = node,
                creatorRouting = mock()
            ),
            savedInstanceState = null,
            customisations = mock()
        )

        val child = builder.build(buildContext)

        plugins.forEach {
            verify(it).onChildBuilt(eq(child.node))
        }
    }

    @Test
    fun `SubtreeChangeAware plugins receive onAttachChildNode()`() {
        val (node, plugins) = testPlugins<SubtreeChangeAware>()
        val childNode = createChildNode(parent = node)

        node.attachChildNode(childNode)

        plugins.forEach {
            verify(it).onChildAttached(eq(childNode))
        }
    }

    @Test
    fun `SubtreeChangeAware plugins receive onDetachChildNode()`() {
        val (node, plugins) = testPlugins<SubtreeChangeAware>()
        val childNode = createChildNode(parent = node)
        node.attachChildNode(childNode)

        node.detachChildNode(childNode, isRecreating = false)

        plugins.forEach {
            verify(it).onChildDetached(eq(childNode))
        }
    }
}
