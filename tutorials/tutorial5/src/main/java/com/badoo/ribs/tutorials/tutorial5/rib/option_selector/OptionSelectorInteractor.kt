package com.badoo.ribs.tutorials.tutorial5.rib.option_selector

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.binder.using
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelector.Output
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorView.Event
import com.badoo.ribs.tutorials.tutorial5.rib.option_selector.OptionSelectorView.ViewModel
import io.reactivex.functions.Consumer

class OptionSelectorInteractor(
    buildParams: BuildParams<Nothing?>,
    private val output: Consumer<Output>,
    options: List<Text>
) : Interactor<OptionSelector, OptionSelectorView>(
    buildParams = buildParams,
    disposables = null
) {

    private val initialViewModel = ViewModel(
        options
    )

    private val viewEventToOutput: (Event) -> Output = {
        TODO()
    }

    override fun onViewCreated(view: OptionSelectorView, viewLifecycle: Lifecycle) {
        view.accept(initialViewModel)
        viewLifecycle.createDestroy {
            bind(view to output using viewEventToOutput)
        }
    }
}
