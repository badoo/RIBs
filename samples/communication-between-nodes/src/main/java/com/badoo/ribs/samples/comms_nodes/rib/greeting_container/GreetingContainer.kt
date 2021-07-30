package com.badoo.ribs.samples.comms_nodes.rib.greeting_container

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation

interface GreetingContainer : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: GreetingContainerView.Factory = GreetingContainerViewImpl.Factory(),
    ) : RibCustomisation
}
