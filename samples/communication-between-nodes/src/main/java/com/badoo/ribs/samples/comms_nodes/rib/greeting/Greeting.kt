package com.badoo.ribs.samples.comms_nodes.rib.greeting

import com.badoo.ribs.android.text.Text
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib

interface Greeting : Rib, Connectable<Greeting.Input, Greeting.Output> {

    interface Dependency

    sealed class Input {
        data class UpdateGreeting(val greeting: Text) : Input()
    }

    sealed class Output {
        object ChangeLanguage : Output()
    }

    class Customisation(
        val viewFactory: GreetingView.Factory = GreetingViewImpl.Factory()
    )
}