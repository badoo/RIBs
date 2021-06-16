package com.badoo.ribs.samples.comms_nodes.rib.language_selector

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelector.Dependency
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelectorBuilder.Params

class LanguageSelectorBuilder(
    private val dependencies: Dependency
) : Builder<Params, LanguageSelector>() {

    data class Params(
        val currentLanguage: Language
    )

    override fun build(buildParams: BuildParams<Params>): LanguageSelector {
        val payload = buildParams.payload.currentLanguage
        val presenter = LanguageSelectorPresenterImpl(
            defaultSelection = getCurrentSelection(payload)
        )
        val viewDependency = object : LanguageSelectorView.Dependency {
            override val presenter: LanguageSelectorPresenter
                get() = presenter
        }
        val customisation = buildParams.getOrDefault(LanguageSelector.Customisation())

        return LanguageSelectorNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory.invoke(viewDependency),
            plugins = listOf(presenter)
        )
    }

    private fun getCurrentSelection(currentLanguage: Language): Int {
        val languageIndex = Language.values().indexOf(currentLanguage)
        return if (languageIndex == -1) {
            0
        } else {
            languageIndex
        }
    }
}
