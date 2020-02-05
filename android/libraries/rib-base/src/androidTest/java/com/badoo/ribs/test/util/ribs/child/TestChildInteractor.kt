package com.badoo.ribs.test.util.ribs.child

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.child.TestChildRouter.Configuration
import com.badoo.ribs.core.BuildContext

class TestChildInteractor(
    buildContext: BuildContext<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, TestChildView>
) : Interactor<Configuration, Configuration, Nothing, TestChildView>(
    buildContext = buildContext,
    router = router,
    disposables = null
)
