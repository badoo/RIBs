package com.badoo.ribs.core.helper

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Rib
import com.nhaarman.mockitokotlin2.mock

class TestNode2(
    identifier: Rib,
    savedInstanceState: Bundle? = null,
    viewFactory: ((ViewGroup) -> TestView?)? = mock()
) : TestNode(
    savedInstanceState = savedInstanceState,
    identifier = identifier,
    viewFactory = viewFactory
) {
}
