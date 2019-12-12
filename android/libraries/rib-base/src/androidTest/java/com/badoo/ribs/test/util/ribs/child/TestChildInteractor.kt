package com.badoo.ribs.test.util.ribs.child

import android.os.Bundle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.test.util.ribs.child.TestChildRouter.Configuration

class TestChildInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Configuration, Nothing, TestChildView>
) : Interactor<TestChildView>(
    savedInstanceState = savedInstanceState,
    disposables = null
)
