package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.plugin.Plugin
import com.nhaarman.mockitokotlin2.mock

open class TestNode(
    buildParams: BuildParams<*> = testBuildParams(),
    viewFactory: ((ViewGroup) -> TestView?)? = TestViewFactory(),
    router: Router<*, *, *, *, TestView> = mock(),
    plugins: List<Plugin> = emptyList()
) : Node<TestView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins + listOf(router)
), TestRib {

    var handleBackPress: Boolean =
        false

    var handleBackPressInvoked: Boolean =
        false

    fun makeActiveBackPressHandler(isActive: Boolean) {
        attachToView(mock())
        handleBackPress = isActive
        markPendingDetach(!isActive)
        markPendingViewDetach(!isActive)
    }

    override fun handleBackPress(): Boolean =
        handleBackPress.also {
            handleBackPressInvoked = true
        }
}
