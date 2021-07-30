package com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.LanguageSelector.Output.LanguageSelected
import com.badoo.ribs.samples.coordinate_multiple_example.rib.language_selector.LanguageSelectorView.ViewModel

interface LanguageSelectorPresenter {
    fun onLanguageConfirmed(selectionIndex: Int)
}

internal class LanguageSelectorPresenterImpl(
    ribAware: RibAware<LanguageSelector> = RibAwareImpl(),
    private val defaultSelection: Int
) : LanguageSelectorPresenter,
    ViewAware<LanguageSelectorView>,
    RibAware<LanguageSelector> by ribAware {

    override fun onViewCreated(view: LanguageSelectorView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.accept(ViewModel(defaultSelection))
    }

    override fun onLanguageConfirmed(selectionIndex: Int) {
        rib.output.accept(LanguageSelected(selectionIndex))
    }
}
