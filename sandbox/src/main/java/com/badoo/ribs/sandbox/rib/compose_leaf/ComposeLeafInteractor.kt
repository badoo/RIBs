package com.badoo.ribs.sandbox.rib.compose_leaf

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.ViewModel

internal class ComposeLeafInteractor(
    private val buildParams: BuildParams<ComposeLeaf.Params>
) : Interactor<ComposeLeaf, ComposeLeafView>(
    buildParams = buildParams
) {
    override fun onViewCreated(view: ComposeLeafView, viewLifecycle: Lifecycle) {
        view.accept(
            ViewModel(i = buildParams.payload.i)
        )
    }
}
