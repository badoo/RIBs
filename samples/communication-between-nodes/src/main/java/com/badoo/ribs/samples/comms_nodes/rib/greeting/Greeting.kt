package com.badoo.ribs.samples.comms_nodes.rib.greeting

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.comms_nodes.app.Language

interface Greeting : Rib, Connectable<Greeting.Input, Greeting.Output> {

    interface Dependency

    sealed class Input {
        data class UpdateGreeting(val language: Language) : Input()
    }

    sealed class Output {
        data class ChangeLanguage(val currentLanguage: Language) : Output()
    }

    class Customisation(
        val viewFactory: GreetingView.Factory = GreetingViewImpl.Factory()
    )
}