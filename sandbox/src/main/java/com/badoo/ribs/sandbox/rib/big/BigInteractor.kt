package com.badoo.ribs.sandbox.rib.big

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.BackStackInteractor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.big.BigRouter.Configuration

class BigInteractor(
    buildParams: BuildParams<*>
) : BackStackInteractor<Big, BigView, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Configuration.Content.Default
) {

    override fun onViewCreated(view: BigView, viewLifecycle: Lifecycle) {
        view.accept(BigView.ViewModel("My id: " + id.replace("${BigInteractor::class.java.name}.", "")))
    }
}
