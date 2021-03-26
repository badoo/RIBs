package com.badoo.ribs.samples.comms_nodes.rib.language_selector.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.comms_nodes.app.Language
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelector
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorNode
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorPresenter
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorPresenterImpl
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorView

class LanguageSelectorBuilder(
    private val languages: List<Language>,
    private val currentLanguage: Language
) : SimpleBuilder<LanguageSelector>() {

    override fun build(buildParams: BuildParams<Nothing?>): LanguageSelector {
        val presenter = LanguageSelectorPresenterImpl(languages = languages, defaultSelection = getCurrentSelection())
        val viewDependency = object : LanguageSelectorView.Dependency {
            override val presenter: LanguageSelectorPresenter
                get() = presenter
        }

        return LanguageSelectorNode(
            buildParams = buildParams,
            viewFactory = LanguageSelector.Customisation().viewFactory.invoke(viewDependency),
            plugins = listOf(presenter)
        )
    }

    private fun getCurrentSelection(): Int {
        val languageIndex = languages.indexOf(currentLanguage)
        return if (languageIndex == -1) {
            0
        } else {
            languageIndex
        }
    }

}