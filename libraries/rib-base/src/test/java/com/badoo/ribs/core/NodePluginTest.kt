package com.badoo.ribs.core

import android.view.ViewGroup
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.helper.testBuildParams
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.routing.Routing
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before

open class NodePluginTest {
    protected lateinit var view: TestView
    protected lateinit var androidView: ViewGroup
    protected lateinit var parentView: RibView
    protected lateinit var viewFactory: TestViewFactory

    interface TestViewFactory : ViewFactory<TestView>

    @Before
    open fun setUp() {
        parentView = mock()
        androidView = mock()
        view = mock { on { androidView }.thenReturn(androidView) }
        viewFactory =
            mock { on { invoke(argThat(predicate = { parent == parentView })) } doReturn view }
    }

    protected inline fun <reified T : Plugin> testPlugins(nbPlugins: Int = 3): Pair<Node<TestView>, List<T>> =
        testPlugins(List<T>(nbPlugins) { mock<T>() })

    protected inline fun <reified T : Plugin> testPlugins(plugins: List<T>): Pair<Node<TestView>, List<T>> {
        val node = createNode(
            viewFactory = viewFactory,
            plugins = plugins
        )

        return node to plugins
    }

    protected fun createNode(
        buildParams: BuildParams<Nothing?> = testBuildParams(),
        viewFactory: TestViewFactory? = this@NodePluginTest.viewFactory,
        plugins: List<Plugin> = emptyList()
    ): Node<TestView> = Node(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins
    )

    protected fun createChildNode(parent: Node<TestView>): TestNode {
        return TestNode(
            buildParams = testBuildParams(
                ancestryInfo = AncestryInfo.Child(parent, Routing(AnyConfiguration))
            ),
            viewFactory = null
        )
    }
}
