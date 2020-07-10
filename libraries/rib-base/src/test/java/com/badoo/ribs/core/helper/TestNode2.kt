package com.badoo.ribs.core.helper

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.nhaarman.mockitokotlin2.mock

class TestNode2(
    buildParams: BuildParams<*> = testBuildParams(),
    viewFactory: ((RibView) -> TestView?)? = mock()
) : TestNode(
    buildParams = buildParams,
    viewFactory = viewFactory
)
