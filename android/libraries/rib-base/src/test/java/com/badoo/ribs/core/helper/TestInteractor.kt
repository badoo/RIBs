package com.badoo.ribs.core.helper

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.builder.BuildParams
import io.reactivex.disposables.Disposable

class TestInteractor(
    private val onViewCreated: (view: TestView, viewLifecycle: Lifecycle) -> Unit = { _, _ -> },
    buildParams: BuildParams<Nothing?> = testBuildParams(),
    disposables: Disposable? = null
) : Interactor<TestRib, TestView>(
    buildParams = buildParams,
    disposables = disposables
) {

    override fun onViewCreated(view: TestView, viewLifecycle: Lifecycle) {
        onViewCreated.invoke(view, viewLifecycle)
    }
}
