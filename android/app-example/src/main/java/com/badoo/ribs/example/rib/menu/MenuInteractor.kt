package com.badoo.ribs.example.rib.menu

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.using
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.menu.MenuRouter.Configuration
import com.badoo.ribs.example.rib.menu.feature.MenuFeature
import com.badoo.ribs.example.rib.menu.mapper.InputToState
import com.badoo.ribs.example.rib.menu.mapper.StateToViewModel
import com.badoo.ribs.example.rib.menu.mapper.ViewEventToOutput
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class MenuInteractor(
    buildParams: BuildParams<Nothing?>,
    router: Router<Configuration, Nothing, Configuration, Nothing, MenuView>,
    private val input: ObservableSource<Menu.Input>,
    private val output: Consumer<Menu.Output>,
    private val feature: MenuFeature
) : Interactor<Configuration, Configuration, Nothing, MenuView>(
    buildParams = buildParams,
    router = router,
    disposables = feature
) {

    override fun onAttach(ribLifecycle: Lifecycle, savedInstanceState: Bundle?) {
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
