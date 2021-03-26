package com.badoo.ribs.samples.comms_nodes.rib.language_selector

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.samples.comms_nodes.app.Language

interface LanguageSelectorPresenter {
    fun onEvent(event: LanguageSelectorView.Event)
}

internal class LanguageSelectorPresenterImpl(
    ribAware: RibAware<LanguageSelector> = RibAwareImpl(),
    private val languages: List<Language>,
    private val defaultSelection: Int
) : LanguageSelectorPresenter,
    ViewAware<LanguageSelectorView>,
    RibAware<LanguageSelector> by ribAware {

    override fun onViewCreated(view: LanguageSelectorView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.accept(LanguageSelectorView.ViewModel(defaultSelection, languages))
    }

    override fun onEvent(event: LanguageSelectorView.Event) {
        when (event) {
            is LanguageSelectorView.Event.LanguageConfirmed -> {
                rib.output.accept(LanguageSelector.Output.LanguageSelected(languages[event.selectionIndex]))
            }
        }
    }
}