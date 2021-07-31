package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting.Greeting.Input
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting.Greeting.Output
import com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.language_selector.Language

interface Greeting : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input {
        data class UpdateGreeting(val selectedIndex: Int) : Input()
    }

    sealed class Output {
        data class LanguageChangeRequested(val currentLanguage: Language) : Output()
    }

    class Customisation(
        val viewFactory: GreetingView.Factory = GreetingViewImpl.Factory()
    ) : RibCustomisation
}
