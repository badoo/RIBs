package com.badoo.ribs.sandbox.rib.menu

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.sandbox.rib.menu.feature.MenuFeature
import com.badoo.ribs.sandbox.rib.menu.mapper.InputToState
import com.badoo.ribs.sandbox.rib.menu.mapper.StateToViewModel
import com.badoo.ribs.sandbox.rib.menu.mapper.ViewEventToOutput
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class MenuInteractor(
    buildParams: BuildParams<Nothing?>,
    private val input: ObservableSource<Menu.Input>,
    private val output: Consumer<Menu.Output>,
    private val feature: MenuFeature
) : Interactor<MenuView>(
    buildParams = buildParams,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle) {
        ribLifecycle.createDestroy {
            bind(input to feature using InputToState)
        }
    }

    override fun onViewCreated(view: MenuView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(feature to view using StateToViewModel)
            bind(view to output using ViewEventToOutput)
        }
    }
}
