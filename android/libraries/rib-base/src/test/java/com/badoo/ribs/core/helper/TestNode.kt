package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.builder.BuildParams
import com.nhaarman.mockitokotlin2.mock

open class TestNode(
    buildParams: BuildParams<*> = testBuildParams(),
    viewFactory: ((ViewGroup) -> TestView?)? = TestViewFactory(),
    router: Router<*, *, *, *, TestView> = mock()
) : Node<TestView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = listOf(router)
) {
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
