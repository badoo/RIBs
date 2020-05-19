package com.badoo.ribs.core.helper

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.disposables.Disposable

class TestInteractor(
    private val onViewCreated: (view: TestView, viewLifecycle: Lifecycle) -> Unit = { _, _ -> },
    buildParams: BuildParams<Nothing?> = testBuildParams(),
    router: Router<TestRouter.Configuration, Nothing, TestRouter.Configuration, Nothing, TestView> = mock(),
    disposables: Disposable? = null
) : Interactor<TestRib, TestView>(
    buildParams = buildParams,
    disposables = disposables
) {

    override fun onViewCreated(view: TestView, viewLifecycle: Lifecycle) {
        onViewCreated.invoke(view, viewLifecycle)
    }
}
