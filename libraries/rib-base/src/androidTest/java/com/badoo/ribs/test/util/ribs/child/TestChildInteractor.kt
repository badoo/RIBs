package com.badoo.ribs.test.util.ribs.child

import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class TestChildInteractor(
    buildParams: BuildParams<Nothing?>
) : Interactor<TestChild, TestChildView>(
    buildParams = buildParams,
    disposables = null
)
