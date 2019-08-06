package com.badoo.ribs.test.util.ribs.child

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.child.TestChildRouter.Configuration
import android.os.Bundle

class TestChildInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, TestChildView>
) : Interactor<Configuration, Configuration, Nothing, TestChildView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
)
