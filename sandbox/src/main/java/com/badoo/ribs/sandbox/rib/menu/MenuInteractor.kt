package com.badoo.ribs.sandbox.rib.menu

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.menu.feature.MenuFeature
import com.badoo.ribs.sandbox.rib.menu.mapper.InputToState
import com.badoo.ribs.sandbox.rib.menu.mapper.StateToViewModel
import com.badoo.ribs.sandbox.rib.menu.mapper.ViewEventToOutput

class MenuInteractor(
    buildParams: BuildParams<Nothing?>,
    private val feature: MenuFeature
) : Interactor<Menu, MenuView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        nodeLifecycle.createDestroy {
            bind(rib.input to feature using InputToState)
        }
    }

    override fun onViewCreated(view: MenuView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to rib.output using ViewEventToOutput)
        }
    }
}
