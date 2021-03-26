package com.badoo.ribs.samples.comms_nodes.rib.language_selector

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.ViewAware

interface LanguageSelectorPresenter {
    fun onEvent(event: LanguageSelectorView.Event)
}

internal class LanguageSelectorPresenterImpl(
    ribAware: RibAware<Node<LanguageSelectorView>> = RibAwareImpl()
) : LanguageSelectorPresenter,
    ViewAware<LanguageSelectorView>,
    RibAware<Node<LanguageSelectorView>> by ribAware {

    private val languages = listOf(Language.English, Language.German, Language.French)

    override fun onViewCreated(view: LanguageSelectorView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.accept(LanguageSelectorView.ViewModel(languages))
    }

    override fun onEvent(event: LanguageSelectorView.Event) {
        when (event) {
            is LanguageSelectorView.Event.LanguageSelected -> {
                // update selection
            }
            is LanguageSelectorView.Event.LanguageConfirmed -> {
                // pop this screen
            }
        }
    }
}

sealed class Language {
    abstract fun displayText(): Text

    object English : Language() {
        override fun displayText(): Text = Text.Plain("English")
    }

    object German : Language() {
        override fun displayText(): Text = Text.Plain("German")
    }

    object French : Language() {
        override fun displayText(): Text = Text.Plain("French")
    }
}