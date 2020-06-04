package com.badoo.ribs.core

import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.core.helper.TestBuilder
import com.badoo.ribs.core.helper.TestRib
import com.badoo.ribs.core.plugin.RibAware
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginRibAwareTest : NodePluginTest() {

    @Test
    fun `RibAware plugins receive init() when created`() {
        val plugins = List<RibAware<TestRib>>(3) { mock() }
        val builder = TestBuilder(plugins)

        builder.build(root(null))

        plugins.forEach {
            verify(it).init(any())
        }
    }
}
