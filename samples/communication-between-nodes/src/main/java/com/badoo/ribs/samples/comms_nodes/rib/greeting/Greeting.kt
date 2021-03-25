package com.badoo.ribs.samples.comms_nodes.rib.greeting

import com.badoo.ribs.android.text.Text
import com.badoo.ribs.core.Rib

interface Greeting : Rib {

    interface Dependency

    sealed class Input {
        data class UpdateGreeting(val greeting: Text) : Input()
    }
}