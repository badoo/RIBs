package com.badoo.ribs.samples.comms_nodes.rib.language_selector

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.LanguageSelector.Output

interface LanguageSelector : Rib, Connectable<Nothing, Output> {

    interface Dependency {
        val languages: List<Language>
    }

    sealed class Output {
        data class LanguageSelected(val language: Language) : Output()
    }

    class Customisation(
        val viewFactory: LanguageSelectorView.Factory = LanguageSelectorViewImpl.Factory()
    ) : RibCustomisation
}
