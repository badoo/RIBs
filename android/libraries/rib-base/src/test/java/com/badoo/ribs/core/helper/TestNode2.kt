package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.BuildContext
import com.nhaarman.mockitokotlin2.mock

class TestNode2(
    buildContext: BuildContext<*> = testBuildContext(),
    viewFactory: ((ViewGroup) -> TestView?)? = mock()
) : TestNode(
    buildContext = buildContext,
    viewFactory = viewFactory
)
