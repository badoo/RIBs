package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Router
import com.nhaarman.mockitokotlin2.mock

open class TestNode(
    buildContext: BuildContext.Resolved<*>,
    viewFactory: ((ViewGroup) -> TestView?)? = mock(),
    router: Router<*, *, *, *, TestView> = mock()
) : Node<TestView>(
    buildContext = buildContext,
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
