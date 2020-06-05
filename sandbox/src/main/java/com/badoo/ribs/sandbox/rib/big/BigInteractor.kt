package com.badoo.ribs.sandbox.rib.big

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams

class BigInteractor(
    buildParams: BuildParams<*>
) : Interactor<Big, BigView>(
    buildParams = buildParams
) {

    override fun onViewCreated(view: BigView, viewLifecycle: Lifecycle) {
        view.accept(BigView.ViewModel("My id: " + requestCodeClientId.replace("${BigInteractor::class.java.name}.", "")))
    }
}
