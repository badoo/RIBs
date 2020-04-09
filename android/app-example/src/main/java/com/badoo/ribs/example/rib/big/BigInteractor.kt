package com.badoo.ribs.example.rib.big

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Interactor

class BigInteractor(
    buildParams: BuildParams<Nothing?>,
    private val router: BigRouter
) : Interactor<BigView>(
    buildParams = buildParams,
    disposables = null
) {

    override fun onViewCreated(view: BigView, viewLifecycle: Lifecycle) {
        view.accept(BigView.ViewModel("My id: " + id.replace("${BigInteractor::class.java.name}.", "")))
    }
}
