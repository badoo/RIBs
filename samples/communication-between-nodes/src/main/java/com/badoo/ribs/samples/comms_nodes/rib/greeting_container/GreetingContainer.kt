package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.samples.comms_nodes.rib.language_selector.Language

interface GreetingContainer : Rib {

    interface Dependency {
        val languages: List<Language>
    }
}
