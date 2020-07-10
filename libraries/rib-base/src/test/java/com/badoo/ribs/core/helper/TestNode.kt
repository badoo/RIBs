package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.nhaarman.mockitokotlin2.mock

open class TestNode(
    buildParams: BuildParams<*> = testBuildParams(),
    viewFactory: ((RibView) -> TestView?)? = TestViewFactory(),
    router: Router<*> = mock(),
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

    fun makeActive(isActive: Boolean) {
        attachToView(mock())
        markPendingDetach(!isActive)
        markPendingViewDetach(!isActive)
    }

    override fun handleBackPress(): Boolean =
        handleBackPress.also {
            handleBackPressInvoked = true
        }
}
