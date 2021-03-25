package com.badoo.ribs.samples.comms_nodes.rib.language_selector

import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.Rib

interface LanguageSelector : Rib {

    interface Dependency

    sealed class Output {
        data class GreetingSelected(val greeting: Text) : Output()
    }
}