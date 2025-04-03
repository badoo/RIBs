package com.badoo.ribs.core

import android.os.Bundle
import com.badoo.ribs.core.plugin.SavesInstanceState
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginSavesInstanceStateTest : NodePluginTest() {

    @Test
    fun `SavesInstanceState plugins receive onSaveInstanceState()`() {
        val (node, plugins) = testPlugins<SavesInstanceState>()
        val bundle = mock<Bundle>()

        node.onSaveInstanceState(bundle)

        plugins.forEach {
            verify(it).onSaveInstanceState(bundle)
        }
    }
}
