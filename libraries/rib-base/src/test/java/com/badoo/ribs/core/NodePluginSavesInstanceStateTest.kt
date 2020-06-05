package com.badoo.ribs.core

import android.os.Bundle
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
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
