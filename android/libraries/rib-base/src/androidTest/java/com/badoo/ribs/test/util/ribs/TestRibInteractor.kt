package com.badoo.ribs.test.util.ribs

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.TestRibRouter.Configuration

class TestRibInteractor(
    router: Router<Configuration, TestRibView>
) : Interactor<Configuration, TestRibView>(
    router = router,
    disposables = null
)
