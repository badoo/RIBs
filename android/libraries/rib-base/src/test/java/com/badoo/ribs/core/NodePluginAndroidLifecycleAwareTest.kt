package com.badoo.ribs.core

import com.badoo.ribs.core.plugin.AndroidLifecycleAware
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodePluginAndroidLifecycleAwareTest : NodePluginTest() {

    @Test
    fun `AndroidLifecycleAware plugins receive onStart()`() {
        val (node, plugins) = testPlugins<AndroidLifecycleAware>()

        node.onStart()

        plugins.forEach {
            verify(it).onStart()
        }
    }

    @Test
    fun `AndroidLifecycleAware plugins receive onStop()`() {
        val (node, plugins) = testPlugins<AndroidLifecycleAware>()

        node.onStop()

        plugins.forEach {
            verify(it).onStop()
        }
    }

    @Test
    fun `AndroidLifecycleAware plugins receive onPause()`() {
        val (node, plugins) = testPlugins<AndroidLifecycleAware>()

        node.onPause()

        plugins.forEach {
            verify(it).onPause()
        }
    }

    @Test
    fun `AndroidLifecycleAware plugins receive onResume()`() {
        val (node, plugins) = testPlugins<AndroidLifecycleAware>()

        node.onResume()

        plugins.forEach {
            verify(it).onResume()
        }
    }
}
