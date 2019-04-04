package com.badoo.ribs.test.util.ribs.child

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.child.TestChildRouter.Configuration

class TestChildInteractor(
    router: Router<Configuration, TestChildView>
) : Interactor<Configuration, TestChildView>(
    router = router,
    disposables = null
)
