package com.badoo.ribs.core.helper

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.disposables.Disposable

class TestInteractor(
    private val onViewCreated: (view: TestView, viewLifecycle: Lifecycle) -> Unit = { _, _ -> },
    buildContext: BuildContext<Nothing?> = testBuildContext(),
    router: Router<TestRouter.Configuration, Nothing, TestRouter.Configuration, Nothing, TestView> = mock(),
    disposables: Disposable? = null
) : Interactor<TestRouter.Configuration, TestRouter.Configuration, Nothing, TestView>(
    buildContext = buildContext,
    router = router,
    disposables = disposables
) {

    override fun onViewCreated(view: TestView, viewLifecycle: Lifecycle) {
        onViewCreated.invoke(view, viewLifecycle)
    }
}
