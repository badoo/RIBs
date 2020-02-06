package com.badoo.ribs.test.util.ribs.child

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.builder.BuildParams

class TestChildInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<TestChildView>(
    buildParams = buildParams,
    disposables = null
)
