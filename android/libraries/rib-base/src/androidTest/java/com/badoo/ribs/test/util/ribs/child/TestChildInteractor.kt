package com.badoo.ribs.test.util.ribs.child

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.child.TestChildRouter.Configuration
import com.badoo.ribs.core.BuildParams

class TestChildInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, TestChildView>
) : Interactor<Configuration, Configuration, Nothing, TestChildView>(
    buildParams = buildParams,
    router = router,
    disposables = null
)
