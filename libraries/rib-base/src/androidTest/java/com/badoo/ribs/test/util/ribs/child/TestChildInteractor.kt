package com.badoo.ribs.test.util.ribs.child

import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class TestChildInteractor(
    buildParams: BuildParams<*>
) : Interactor<TestChild, TestChildView>(
    buildParams = buildParams
)
