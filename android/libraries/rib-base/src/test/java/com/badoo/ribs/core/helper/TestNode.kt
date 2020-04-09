package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.nhaarman.mockitokotlin2.mock

open class TestNode(
    buildParams: BuildParams<*> = testBuildParams(),
    identifier: Rib = mock(),
    viewFactory: ((ViewGroup) -> TestView?)? = TestViewFactory(),
    router: Router<*, *, *, *, TestView> = mock()
) : Node<TestView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = mock()
) {
    var handleBackPress: Boolean =
        false

    var handleBackPressInvoked: Boolean =
        false

    override fun handleBackPress(): Boolean =
        handleBackPress.also {
            handleBackPressInvoked = true
        }
}
