package com.badoo.ribs.core.helper

import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import io.reactivex.disposables.Disposable
import android.os.Bundle

class TestInteractor(
    savedInstanceState: Bundle? = null,
    router: Router<TestRouter.Configuration, Nothing, TestRouter.Configuration, Nothing, TestView>,
    disposables: Disposable?
) : Interactor<TestRouter.Configuration, TestRouter.Configuration, Nothing, TestView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = disposables
)
