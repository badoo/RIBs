package com.badoo.ribs.core.helper

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory
import org.mockito.kotlin.mock

class TestNode2(
    buildParams: BuildParams<*> = testBuildParams(),
    viewFactory: ViewFactory<TestView>? = mock()
) : TestNode(
    buildParams = buildParams,
    viewFactory = viewFactory
)
