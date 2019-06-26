package com.badoo.ribs.core.helper

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.nhaarman.mockitokotlin2.mock
import com.badoo.ribs.core.view.ViewFactory

class TestNode(
    identifier: Rib,
    viewFactory: ViewFactory<TestView>? = mock()
) : Node<TestView>(
    identifier = identifier,
    viewFactory = viewFactory,
    router = mock(),
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
