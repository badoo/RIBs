package com.badoo.ribs.core.helper

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class TestInteractor(
    private val onViewCreated: (view: TestView, viewLifecycle: Lifecycle) -> Unit = { _, _ -> },
    buildParams: BuildParams<Nothing?> = testBuildParams()
) : Interactor<TestRib, TestView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: TestView, viewLifecycle: Lifecycle) {
        onViewCreated.invoke(view, viewLifecycle)
    }
}
