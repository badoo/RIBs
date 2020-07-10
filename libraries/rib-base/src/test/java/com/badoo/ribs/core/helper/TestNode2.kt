package com.badoo.ribs.core.helper

import android.view.ViewGroup
import com.badoo.ribs.core.modality.BuildParams
import com.nhaarman.mockitokotlin2.mock

class TestNode2(
    buildParams: BuildParams<*> = testBuildParams(),
    viewFactory: ((RibView) -> TestView?)? = mock()
) : TestNode(
    buildParams = buildParams,
    viewFactory = viewFactory
)
