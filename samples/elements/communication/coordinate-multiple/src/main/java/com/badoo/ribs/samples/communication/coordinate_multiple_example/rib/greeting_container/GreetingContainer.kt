package com.badoo.ribs.samples.communication.coordinate_multiple_example.rib.greeting_container

import com.badoo.ribs.core.Rib
import com.bumble.appyx.utils.customisations.NodeCustomisation

interface GreetingContainer : Rib {

    interface Dependency

    class Customisation(
        val viewFactory: GreetingContainerView.Factory = GreetingContainerViewImpl.Factory(),
    ) : NodeCustomisation
}
